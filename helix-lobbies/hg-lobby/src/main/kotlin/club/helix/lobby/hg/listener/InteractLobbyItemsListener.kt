package club.helix.lobby.hg.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.components.server.HelixServer
import club.helix.lobby.provider.inventory.LobbiesInventory
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class InteractLobbyItemsListener: Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(it.item, "lobby-item", "select-lobby")
    }?.apply {
        isCancelled = true
        LobbiesInventory(HelixServer.HG).open(event.player)
    }
}