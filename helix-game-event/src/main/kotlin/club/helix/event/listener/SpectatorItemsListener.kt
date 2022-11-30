package club.helix.event.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.connect
import club.helix.event.EventPlugin
import club.helix.event.inventory.SpectatorPlayersInventory
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class SpectatorItemsListener(private val plugin: EventPlugin): Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(it.item, "spectator-item")
    }?.apply {
        isCancelled = true

        when (ItemBuilder.get(item, "spectator-item")) {
            "players" -> SpectatorPlayersInventory(plugin.game.players).open(player)
            "back-to-lobby" -> {
                val availableLobby = plugin.apiBukkit.currentServer.findAvailableLobby()
                    ?: return@apply player.sendMessage("§cNão há um lobby disponível.")
                player.connect(availableLobby, true)
            }
        }
    }
}