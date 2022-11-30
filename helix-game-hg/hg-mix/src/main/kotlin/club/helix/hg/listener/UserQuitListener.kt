package club.helix.hg.listener

import club.helix.hg.HgPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class UserQuitListener(private val plugin: HgPlugin): Listener {

    @EventHandler fun onQuit(event: PlayerQuitEvent) = event.apply {
        plugin.game.removePlayer(player)
    }
}