package club.helix.lobby.pvp.listener

import club.helix.components.server.HelixServer
import club.helix.bukkit.builder.ItemBuilder
import club.helix.lobby.provider.inventory.LobbiesInventory
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class InteractLobbyItemsListener: Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.hasItem() && ItemBuilder.has(event.item, "lobby-item", "select-lobby")) {
            event.isCancelled = true
            LobbiesInventory(HelixServer.PVP).open(event.player)
        }
    }
}