package club.helix.event.listener.config

import club.helix.event.EventPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class ItemDropConfigListener(private val plugin: EventPlugin): Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onItemDrop(event: PlayerDropItemEvent) = event.takeIf {
        plugin.game.getConfig("item-drop") == false
    }?.apply { isCancelled = true }
}