package club.helix.duels.uhc.listener

import club.helix.bukkit.builder.ItemBuilder
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class InstantAppleListener: Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(event.item, "instant-apple")
    }?.apply {
        isCancelled = true

        val amount = item.amount
        if ((amount - 1) > 0) {
            player.itemInHand = player.itemInHand.apply { this.amount-- }
        }else {
            player.itemInHand = ItemStack(Material.AIR)
        }

        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 10 * 20, 0))
        player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 1 * (60 * 20), 0))
        player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 2))
    }
}