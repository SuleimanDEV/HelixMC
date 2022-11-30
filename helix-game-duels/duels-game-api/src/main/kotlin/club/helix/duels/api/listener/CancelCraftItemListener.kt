package club.helix.duels.api.listener

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack

class CancelCraftItemListener: Listener {

    @EventHandler fun onCraftItem(event: PrepareItemCraftEvent) = event.takeIf {
        it.recipe.result.type == Material.WORKBENCH
    }?.apply { inventory.result = ItemStack(Material.AIR) }
}