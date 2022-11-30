package club.helix.hg.listener

import club.helix.components.server.provider.HardcoreGamesProvider
import club.helix.hg.HgPlugin
import club.helix.hg.player.GamePlayerType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserJoinListener(private val plugin: HgPlugin): Listener {

    @EventHandler fun onJoin(event: PlayerJoinEvent) = event.apply {
        joinMessage = null
        plugin.game.putPlayer(player, if (plugin.game.state.ordinal <= HardcoreGamesProvider.State.INVINCIBILITY.ordinal)
            GamePlayerType.PLAYING else GamePlayerType.SPECTATOR)
    }
}