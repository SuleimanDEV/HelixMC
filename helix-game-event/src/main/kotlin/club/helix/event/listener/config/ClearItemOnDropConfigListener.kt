package club.helix.event.listener.config

import club.helix.event.EventPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class ClearItemOnDropConfigListener(private val plugin: EventPlugin): Listener {

    @EventHandler fun onDrop(event: PlayerDropItemEvent) = event.takeIf {
        plugin.game.getConfig<Boolean>("clear-item-on-drop") == true
    }?.apply { itemDrop.remove() }
}