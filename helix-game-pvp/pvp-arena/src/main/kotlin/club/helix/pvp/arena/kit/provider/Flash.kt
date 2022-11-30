package club.helix.pvp.arena.kit.provider

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.kit.KitHandlerCooldown
import club.helix.pvp.arena.kit.provider.event.PlayerFlashEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag

class Flash: KitHandlerCooldown(
    "flash-kit", 10
) {

    override fun apply(player: Player): Unit = player.inventory.run {
        addItem(ItemBuilder()
            .type(Material.REDSTONE_TORCH_ON)
            .displayName("§aTeleportar!")
            .identify("cancel-drop")
            .identify("kit", "flash")
            .unbreakable()
            .itemFlags(ItemFlag.HIDE_UNBREAKABLE)
            .toItem
        )
    }

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        valid(it.player) && it.hasItem() && ItemBuilder.has(event.item, "kit", "flash")
    }?.player?.apply {
        event.isCancelled = true

        if (hasCooldownOrCreate(this)) {
            return@apply sendCooldownMessage(this)
        }

        val flashEvent = PlayerFlashEvent(player)
        Bukkit.getPluginManager().callEvent(flashEvent)

        if (flashEvent.isCancelled) return@apply
        val block = getTargetBlock(setOf(Material.AIR), 100)?.takeIf { it.type != Material.AIR } ?: return@apply
        val teleport = block.getRelative(BlockFace.UP).takeIf { it.type == Material.AIR }?.location ?: block.location

        fallDistance = -1f
        player.teleport(teleport.apply { yaw = location.yaw; pitch = location.pitch })
        block.world.strikeLightningEffect(block.location)
    }
}