package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandler
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent

class Fireman: KitHandler() {

    @EventHandler (ignoreCancelled = true)
    fun onEntityDamage(event: EntityDamageEvent) = event.takeIf {
        it.entity is Player && valid(it.entity as Player) &&
                (it.cause == EntityDamageEvent.DamageCause.FIRE
                        || it.cause == EntityDamageEvent.DamageCause.FIRE_TICK
                        || it.cause == EntityDamageEvent.DamageCause.LAVA)
    }?.apply { isCancelled = true }

    @EventHandler fun onMove(event: PlayerMoveEvent) = event.takeIf {
        valid(it.player) && it.player.location.block.type.let { material ->
            material == Material.WATER || material == Material.WATER_LILY || material == Material.STATIONARY_WATER }
    }?.apply { player.damage(1.5) }
}