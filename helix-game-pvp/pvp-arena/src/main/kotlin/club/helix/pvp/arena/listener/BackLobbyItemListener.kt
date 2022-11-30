package club.helix.pvp.arena.listener

import club.helix.components.server.HelixServer
import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.connect
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class BackLobbyItemListener: Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(it.item, "spawn-item", "back-to-lobby")
    }?.run {
        isCancelled = true

        HelixServer.PVP.findAvailable(HelixServer.Category.LOBBY)?.apply {
            player.connect(this)
        } ?: player.sendMessage("§cNão há um lobby disponível.")
    }
}