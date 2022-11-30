package club.helix.pvp.lava.listener

import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class UserDamageListener: Listener {

    @EventHandler fun onDamage(event: EntityDamageEvent) = event.takeIf {
        it.entity is Player && it.cause != EntityDamageEvent.DamageCause.LAVA &&
                it.cause != EntityDamageEvent.DamageCause.PROJECTILE &&
                it.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK
    }?.apply { isCancelled = true }

    @EventHandler fun onDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        it.entity is Player && (it.damager as? Player)?.let { damager -> damager.gameMode != GameMode.CREATIVE } == true
    }?.apply { isCancelled = true }
}