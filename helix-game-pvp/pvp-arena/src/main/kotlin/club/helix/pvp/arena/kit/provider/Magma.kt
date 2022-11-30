package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandler
import club.helix.pvp.arena.kotlin.player.allowedPvP
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

class Magma: KitHandler() {

    @EventHandler fun onDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        it.entity is Player && it.damager is Player && (it.entity as Player).allowedPvP && valid(it.damager as Player)
    }?.takeIf { Random().nextInt(100) <= 18 }?.apply { entity.fireTicks = 4 * 20 }
}