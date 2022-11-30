package club.helix.event

import club.helix.bukkit.kotlin.player.connect
import club.helix.event.event.GameStateCallEvent
import club.helix.event.scoreboard.PlayingScoreboard
import club.helix.event.scoreboard.WhitelistScoreboard
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class GameRunnable(private val plugin: EventPlugin): BukkitRunnable() {

    private val game = plugin.game

    override fun run() {
        Bukkit.getPluginManager().callEvent(GameStateCallEvent(game))
        Bukkit.getOnlinePlayers().forEach(game.scoreboard::update)

        when (game.state) {
            EventGame.State.WAITING -> {
                game.time--

                if (game.time < 0) {
                    game.changeState(EventGame.State.PLAYING)
                    game.changeScoreboard(PlayingScoreboard(game))
                }
            }
            EventGame.State.PLAYING -> {
                game.time++
            }
            EventGame.State.ENDED -> {
                if (game.time <= 0 && game.time % 2 == 0) {
                    val onlinePlayers = Bukkit.getOnlinePlayers().filter { !it.hasPermission("helix.event.notify") }
                    val availableLobby = plugin.apiBukkit.currentServer.findAvailableLobby()
                        ?: return onlinePlayers.forEach { it.kickPlayer("§cO evento finalizou!") }

                    onlinePlayers.forEach { it.connect(availableLobby) }
                }

                if (game.time <= -3) {
                    game.changeState(EventGame.State.WHITELIST)
                    game.changeScoreboard(WhitelistScoreboard(game))
                    plugin.changeConfigByServer("build", false)
                    plugin.changeConfigByServer("damage", false)
                }
                game.time--
            }
            else -> {}
        }
    }
}