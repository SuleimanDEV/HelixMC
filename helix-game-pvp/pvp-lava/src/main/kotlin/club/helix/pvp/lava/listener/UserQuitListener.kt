package club.helix.pvp.lava.listener

import club.helix.pvp.lava.kotlin.player.unregisterLavaPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class UserQuitListener: Listener {

    @EventHandler fun onQuit(event: PlayerQuitEvent) = event.player.apply {
        event.quitMessage = null
        unregisterLavaPlayer()
    }
}