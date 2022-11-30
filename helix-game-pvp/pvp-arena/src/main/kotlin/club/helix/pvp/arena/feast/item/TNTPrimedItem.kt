package club.helix.pvp.arena.feast.item

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.PvPArena
import org.bukkit.Material
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable

class TNTPrimedItem(private val plugin: PvPArena): Listener {

    @EventHandler
    fun onInteractTnt(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(event.item, "tnt-primed") &&
                (it.action == Action.RIGHT_CLICK_AIR || it.action == Action.RIGHT_CLICK_BLOCK)
    }?.apply {
        isCancelled = true

        if (item.amount > 1) {
            player.itemInHand = item.apply { this.amount-- }
        }else {
            player.itemInHand = ItemStack(Material.AIR)
        }

        val primedTnt = player.world.spawn(player.location, TNTPrimed::class.java).apply {
            fuseTicks = 4 * 20
            setIsIncendiary(false)
            setMetadata("tnt-primed", FixedMetadataValue(plugin, ""))
            isCustomNameVisible = true

            object: BukkitRunnable() {
                override fun run() {
                    if (isDead || !isValid) return cancel()

                    customName = "§c${fuseTicks.toDouble() / 20.0}s"
                }
            }.runTaskTimer(plugin, 0, 5)
        }

        primedTnt.velocity = player.eyeLocation.direction.multiply(0.8).setY(0.3)
    }

    @EventHandler fun onEntityExplode(event: EntityExplodeEvent) = event.takeIf {
        it.entity.hasMetadata("tnt-primed") }?.apply { blockList().clear() }
}