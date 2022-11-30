package club.helix.pvp.lava.listener

import club.helix.pvp.lava.PvPLava
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class UserDeathListener(private val plugin: PvPLava): Listener {

    @EventHandler fun onPlayerDeath(event: PlayerDeathEvent) = event.apply {
        deathMessage = null
        entity.spigot().respawn()
        drops.clear()
        droppedExp = 0
        newExp = 0
        newLevel = 0

        plugin.server.scheduler.runTaskLater(plugin, {
            plugin.serverSpawn.send(entity)
        }, 10)
    }
}