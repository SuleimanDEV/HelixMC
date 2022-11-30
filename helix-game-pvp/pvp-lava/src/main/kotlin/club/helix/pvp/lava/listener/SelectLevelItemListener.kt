package club.helix.pvp.lava.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.teleport
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.pvp.lava.LavaLevel
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class SelectLevelItemListener: Listener {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("select-level")
            .title("Selecionar Nível")
            .size(3,9)
            .provider(Provider())
            .build()
    }



    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(it.item, "spawn-item", "select-level")
    }?.run { isCancelled = true; inventory.open(player) }

    private class Provider: InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            val slots = mutableListOf(1, 3, 5, 7)

            LavaLevel.values().forEach { level ->
                contents.set(1, slots.removeAt(0), ClickableItem.of(
                    ItemBuilder()
                        .type(level.icon.type)
                        .data(level.icon.data.data.toInt())
                        .displayName(level.displayName)
                        .lore("§7Clique para ir.")
                        .toItem
                ) { player.teleport(level.location) })
            }
        }
    }
}