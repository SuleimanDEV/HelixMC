package club.helix.pvp.arena.kit.provider

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.kit.KitHandlerCooldown
import club.helix.pvp.arena.kotlin.player.allowedPvP
import club.helix.pvp.arena.kotlin.player.hasSelectedKit
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

class Kangaroo: KitHandlerCooldown("kangaroo-kit", 5) {
    companion object {
        private val delayPlayers = mutableListOf<String>()
    }
    override fun apply(player: Player): Unit = player.inventory.run {
        addItem(ItemBuilder()
            .type(Material.FIREWORK)
            .displayName("§aPular!")
            .identify("cancel-drop")
            .identify("kit", "kangaroo")
            .toItem
        )
    }

    @EventHandler fun onFireworkInteract(event: PlayerInteractEvent) = event.takeIf {
        valid(it.player) && it.hasItem() && ItemBuilder.has(event.item, "kit", "kangaroo")
    }?.player?.apply {
        event.isCancelled = true

        if (hasCooldown(player)) return@apply sendCooldownMessage(player)

        if (delayPlayers.contains(name)) return@apply
        delayPlayers.add(name)

        velocity = location.direction.setY(if (isSneaking) 0.22f else 1.25f)
            .multiply(if (isSneaking) 2.45f else 0.9f)
        fallDistance = -1f
    }

    @EventHandler (ignoreCancelled = true)
    fun onPlayerMove(event: PlayerMoveEvent) = event.takeIf {
        delayPlayers.contains(it.player.name) && it.player.location.block.getRelative(BlockFace.DOWN).type != Material.AIR
    }?.apply { delayPlayers.remove(player.name) }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onPlayerAttackCooldown(event: EntityDamageByEntityEvent) = event.takeIf {
        (it.entity as? Player)?.let { entity -> valid(entity) } == true &&
                it.damager is Player
    }?.apply { applyCooldown(entity as Player) }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onFallDamage(event: EntityDamageEvent) = event.takeIf {
        it.entity is Player && (it.entity as Player).allowedPvP && (it.entity as Player).hasSelectedKit(this)
                && it.cause == EntityDamageEvent.DamageCause.FALL
    }?.takeIf { it.damage >= 7.0 }?.apply { damage = 7.0 }
}