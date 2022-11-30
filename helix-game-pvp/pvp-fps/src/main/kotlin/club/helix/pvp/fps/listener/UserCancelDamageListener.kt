package club.helix.pvp.fps.listener

import club.helix.pvp.fps.kotlin.player.allowedPvP
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class UserCancelDamageListener: Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onReceiveDamage(event: EntityDamageEvent) = event.takeIf {
        it.entity is Player && !(it.entity as Player).allowedPvP
    }?.apply { isCancelled = true }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onReceiveDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        it.damager is Player && (it.entity as? Player)?.allowedPvP == false
    }?.apply { isCancelled = true }
}