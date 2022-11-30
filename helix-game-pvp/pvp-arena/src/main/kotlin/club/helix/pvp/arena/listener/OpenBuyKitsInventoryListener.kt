package club.helix.pvp.arena.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.inventory.BuyKitsInventory
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class OpenBuyKitsInventoryListener(val plugin: PvPArena): Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(event.item, "spawn-item", "buy-kits")
    }?.run {
        isCancelled = true
        BuyKitsInventory(plugin.apiBukkit.components).open(player)
    }
}