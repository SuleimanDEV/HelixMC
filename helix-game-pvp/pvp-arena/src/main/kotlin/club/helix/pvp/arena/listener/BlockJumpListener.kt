package club.helix.pvp.arena.listener

import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent

class BlockJumpListener: Listener {
    companion object {
        private val jump = mutableSetOf<String>()
    }

    @EventHandler fun onMove(event: PlayerMoveEvent) = event.player.run {
        val block = event.player.location.block.getRelative(BlockFace.DOWN).type

        if (block == Material.SPONGE) {
            velocity = location.direction.multiply(0.0).setY(5.5)
            jump.add(name)
        }
        if (block == Material.EMERALD_BLOCK) {
            velocity = event.player.eyeLocation.direction.multiply(2.8).setY(0.5)
            fallDistance = -1.0f
            jump.add(name)
        }
    }

    @EventHandler fun onFallDamage(event: EntityDamageEvent) = event.takeIf {
        it.cause == EntityDamageEvent.DamageCause.FALL && jump.contains(it.entity.name)
    }?.apply { isCancelled = true; jump.remove(entity.name) }
}