package club.helix.lobby.provider.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.lobby.provider.ProviderLobby
import club.helix.lobby.provider.inventory.ProfileInventory
import club.helix.lobby.provider.inventory.ServersInventory
import club.helix.lobby.provider.inventory.collectible.CollectiblesInventory
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class InteractLobbyItemsListener(private val plugin: ProviderLobby): Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(event.item, "lobby-item")
    }?.apply {
        event.isCancelled = true

        when(ItemBuilder.get(item, "lobby-item") ?: return@apply) {
            "select-game" -> ServersInventory.open(player)
            "profile" -> ProfileInventory(plugin.apiBukkit.components).open(player)
            "collectibles" -> CollectiblesInventory(plugin.collectibleManager).open(player)
        }
    }
}