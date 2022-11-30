package club.helix.pvp.arena.kit.provider

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.kit.KitHandlerCooldown
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag

class Thor: KitHandlerCooldown("thor-kit", 6) {

    override fun apply(player: Player): Unit = player.inventory.run {
        addItem(ItemBuilder()
            .type(Material.WOOD_AXE)
            .displayName("§aCaboom!")
            .itemFlags(*ItemFlag.values())
            .identify("cancel-drop")
            .identify("kit", "thor")
            .unbreakable()
            .itemFlags(ItemFlag.HIDE_UNBREAKABLE)
            .toItem
        )
    }

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        valid(it.player) && event.hasItem() && it.clickedBlock != null
                && ItemBuilder.has(event.item, "kit", "thor")
    }?.apply {
        isCancelled = true

        if (hasCooldownOrCreate(player)) return@apply sendCooldownMessage(player)
        player.world.strikeLightningEffect(clickedBlock.location)

        var power = 6.0
        clickedBlock.world.getNearbyEntities(clickedBlock.location, 3.0, 3.0, 3.0).filterIsInstance<LivingEntity>().forEach {
            it.damage(power, player)
            it.fireTicks = power.toInt() * 8
            power -= 3.0
        }
    }
}