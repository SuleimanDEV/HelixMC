package club.helix.bukkit.listener

import club.helix.bukkit.builder.ItemBuilder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

class ItemBuilderListener: Listener {

    @EventHandler fun cancelBreak(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(it.item, "cancel-break")
    }?.apply { event.item.durability = -1 }

    @EventHandler fun cancelClick(event: InventoryClickEvent) = event.currentItem?.takeIf {
        ItemBuilder.has(event.currentItem, "cancel-click") }?.let { event.isCancelled = true }

    @EventHandler fun cancelDrop(event: PlayerDropItemEvent) = event.itemDrop.itemStack.takeIf {
        ItemBuilder.has(it, "cancel-drop") }?.let { event.isCancelled = true }
}