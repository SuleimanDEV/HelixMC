package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandler
import club.helix.pvp.arena.kotlin.player.hasSelectedKit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class Turtle: KitHandler() {

    @EventHandler (ignoreCancelled = true)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        (it.entity as? Player)?.let { entity ->
            entity.hasSelectedKit(this) && entity.isSneaking
        } == true
    }?.apply { damage -= 1 }
}