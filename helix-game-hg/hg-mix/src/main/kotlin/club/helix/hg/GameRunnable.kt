package club.helix.hg

import club.helix.bukkit.kotlin.player.connect
import club.helix.components.server.provider.HardcoreGamesProvider
import club.helix.hg.player.event.GameStateCallEvent
import club.helix.hg.scoreboard.InvincibilityScoreboard
import club.helix.hg.scoreboard.PlayingScoreboard
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class GameRunnable(private val plugin: HgPlugin): BukkitRunnable() {

    private val game = plugin.game

    override fun run() {
        Bukkit.getPluginManager().callEvent(GameStateCallEvent(game))
        Bukkit.getOnlinePlayers().forEach(game.scoreboard::update)

        when (game.state) {
            HardcoreGamesProvider.State.WAITING -> {
                game.time--

                if (game.time < 0) {
                    game.changeState(HardcoreGamesProvider.State.INVINCIBILITY)
                    game.changeScoreboard(InvincibilityScoreboard(game))
                }
            }
            HardcoreGamesProvider.State.INVINCIBILITY -> {
                game.time--

                if (game.time < 0) {
                    game.changeState(HardcoreGamesProvider.State.PLAYING)
                    game.changeScoreboard(PlayingScoreboard(game))
                }
            }
            HardcoreGamesProvider.State.PLAYING -> {
                game.time++
            }
            HardcoreGamesProvider.State.ENDED -> {
                if (game.time <= 0 && game.time % 2 == 0) {
                    val onlinePlayers = Bukkit.getOnlinePlayers().filter { !it.hasPermission("helix.event.notify") }
                    val availableLobby = plugin.apiBukkit.currentServer.findAvailableLobby()
                        ?: return onlinePlayers.forEach { it.kickPlayer("§cO evento finalizou!") }

                    onlinePlayers.forEach { it.connect(availableLobby) }
                }
            }
            else -> {}
        }
        plugin.serverProvider.apply {
            time = game.time
            state = game.state
            plugin.apiBukkit.components.callUpdateServer(this)
        }
    }
}