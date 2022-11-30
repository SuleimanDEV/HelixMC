package club.helix.pvp.arena.listener

import club.helix.pvp.arena.PvPArena
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class ClearArenaDropsListener(private val plugin: PvPArena): Listener {

    @EventHandler fun onItemDrop(event: PlayerDropItemEvent) = plugin.server.scheduler
        .runTaskLaterAsynchronously(plugin, { event.itemDrop?.remove() }, 3 * 20)
}