package club.helix.pvp.arena.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class ChangeLavaDamageListener: Listener {

    @EventHandler fun onDamage(event: EntityDamageEvent) = event.takeIf {
        it.cause == EntityDamageEvent.DamageCause.LAVA
    }?.apply { damage = 3.0}
}