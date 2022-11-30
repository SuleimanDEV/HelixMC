package club.helix.event.listener

import club.helix.event.EventGame
import club.helix.event.EventPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class UserQuitListener(private val plugin: EventPlugin): Listener {

    @EventHandler fun onQuit(event: PlayerQuitEvent) = event.apply {
        val game = plugin.game

        quitMessage = game.state.takeIf {
            it == EventGame.State.WAITING
        }?.run {
            val maxPlayers = game.getConfig<Int>("max-players") ?: 100
            "§c${player.name} saiu do evento! (${game.players.size}/$maxPlayers)"
        }

        game.removePlayer(player)
    }
}