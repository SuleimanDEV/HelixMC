package club.helix.event.listener

import club.helix.event.EventGame
import club.helix.event.EventPlugin
import club.helix.event.player.GamePlayerType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserJoinListener(private val plugin: EventPlugin): Listener {

    @EventHandler fun onJoin(event: PlayerJoinEvent) = event.apply {
        joinMessage = null
        plugin.game.putPlayer(player, if (plugin.game.state.ordinal <= EventGame.State.WAITING.ordinal)
            GamePlayerType.PLAYING else GamePlayerType.SPECTATOR)
    }
}