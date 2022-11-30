package club.helix.duels.api.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.connect
import club.helix.components.server.HelixServer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class BackLobbyItemListener: Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(it.item, "back-to-lobby-item")
    }?.run {
        isCancelled = true

        HelixServer.DUELS.findAvailable(HelixServer.Category.LOBBY)?.apply {
            player.connect(this)
        } ?: player.sendMessage("§cNão há um lobby disponível.")
    }
}