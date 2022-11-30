package club.helix.bukkit.listener

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.event.PlayerDeathEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.util.Vector

class UserDeathHandleListener(private val plugin: HelixBukkit): Listener {

    @EventHandler fun onUserDeath(event: PlayerDeathEvent) = Bukkit.getScheduler().runTaskLater(plugin, {
        event.player.apply {
            spigot().respawn()
            fireTicks = 0
            velocity = Vector()
        }
    }, 0.5.toLong())
}