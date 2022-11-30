package club.helix.pvp.arena.kit.provider

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.kit.KitHandlerCooldown
import club.helix.pvp.arena.kotlin.player.hasSelectedKit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Milkman: KitHandlerCooldown("milkman-kit", 35) {

    override fun apply(player: Player): Unit = player.inventory.run {
        addItem(ItemBuilder()
            .type(Material.MILK_BUCKET)
            .displayName("§aLeite Mágico")
            .identify("cancel-drop")
            .identify("kit", "milkman")
            .toItem
        )
    }

    @EventHandler fun onInteract(event: PlayerItemConsumeEvent) = event.takeIf {
        it.player.hasSelectedKit(this) && ItemBuilder.has(event.item, "kit", "milkman")
    }?.apply {
        isCancelled = true
        if (hasCooldownOrCreate(player)) return@apply sendCooldownMessage(player)

        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 25 * 20, 0))
        player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 25 * 20, 1))
        player.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 25 * 20, 0))
    }
}