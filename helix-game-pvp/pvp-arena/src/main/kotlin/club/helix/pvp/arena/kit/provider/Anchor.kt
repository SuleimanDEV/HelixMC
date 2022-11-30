package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandler
import club.helix.pvp.arena.kotlin.player.allowedPvP
import club.helix.pvp.arena.kotlin.player.hasSelectedKit
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.util.Vector

class Anchor: KitHandler() {

    @EventHandler fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        it.entity is Player && it.damager is Player && (it.entity as Player).allowedPvP && (it.damager as Player).allowedPvP
                && ((it.damager as Player).hasSelectedKit(this) || (it.entity as Player).hasSelectedKit(this))
    }?.apply {
        val vector = Vector(0.0, 0.0, 0.0).apply {
            entity.velocity = this; damager.velocity = this
        }
        Bukkit.getScheduler().runTaskLater(plugin, {
            entity.velocity = vector; damager.velocity = vector
            (entity as Player).playSound(entity.location, Sound.ANVIL_LAND, 10.0f, 10.0f)
            (damager as Player).playSound(damager.location, Sound.ANVIL_LAND, 10.0f, 10.0f)
        }, 1)
    }
}