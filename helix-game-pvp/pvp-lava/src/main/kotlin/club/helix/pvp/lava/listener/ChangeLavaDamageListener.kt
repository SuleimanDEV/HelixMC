package club.helix.pvp.lava.listener

import club.helix.pvp.lava.kotlin.player.lavaPlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class ChangeLavaDamageListener: Listener {

    @EventHandler fun onDamage(event: EntityDamageEvent) = event.takeIf {
        it.entity is Player && it.cause == EntityDamageEvent.DamageCause.LAVA
    }?.apply { damage = (entity as Player).lavaPlayer.lavaDamage }
}