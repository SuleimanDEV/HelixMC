package club.helix.duels.sumo

import club.helix.components.server.HelixServer
import club.helix.bukkit.kotlin.player.connect
import club.helix.duels.api.event.GameStartEvent
import club.helix.duels.api.event.StartingStateTaskEvent
import club.helix.duels.api.game.DuelsGameState
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class SumoGameRunnable(
    private val plugin: SumoPlugin
): BukkitRunnable() {

    override fun run() = plugin.duelsAPI.games.forEach { game ->
        when (game.state) {
            DuelsGameState.WAITING -> {
                if (game.playingPlayers.size < game.maxPlayers && game.time != 0) {
                    game.time = 0
                }
                if (game.playingPlayers.size < game.maxPlayers) return

                if (game.time <= 0) {
                    game.time = 5
                    return game.changeState(DuelsGameState.STARTING)
                }
                game.time--
            }
            DuelsGameState.STARTING -> {
                val event = StartingStateTaskEvent(game)
                Bukkit.getPluginManager().callEvent(event)
                if (event.isCancelled) return

                if (game.time <= 0) {
                    game.changeState(DuelsGameState.PLAYING)
                    game.time = 0
                    return Bukkit.getPluginManager().callEvent(GameStartEvent(game))
                }

                game.time--
            }
            DuelsGameState.PLAYING -> {
                game.time++
            }
            DuelsGameState.ENDED -> {
                game.time--

                if (game.time == 0 || game.time == -4) {
                    val availableLobby = HelixServer.DUELS.findAvailable(HelixServer.Category.LOBBY)

                    game.allPlayers().filter(Player::isOnline).forEach {
                        availableLobby?.apply { it.connect(this) }
                            ?: it.kickPlayer("§cNão há um lobby disponível.")
                    }
                }else if (game.time < -2) {
                    game.allPlayers().forEach {
                        it.kickPlayer("§cDUELS: não foi possível voltar ao lobby.")
                    }
                }
            }
        }
        game.takeIf { it.state != DuelsGameState.ENDED }?.run {
            allPlayers().filter(Player::isOnline)
                .forEach { game.scoreboard.update(it) }
        }
    }
}