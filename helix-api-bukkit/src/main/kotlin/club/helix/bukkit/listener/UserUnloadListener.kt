package club.helix.bukkit.listener

import club.helix.bukkit.HelixBukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class UserUnloadListener(private val apiBukkit: HelixBukkit): Listener {

    @EventHandler (priority = EventPriority.MONITOR)
    fun onQuit(event: PlayerQuitEvent) = event.apply {
        HelixBukkit.instance.permissionManager.reset(player)
        apiBukkit.components.userManager.removeUser(player.name)
    }
}