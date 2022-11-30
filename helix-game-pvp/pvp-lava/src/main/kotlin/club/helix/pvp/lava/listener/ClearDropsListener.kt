package club.helix.pvp.lava.listener

import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class ClearDropsListener: Listener {

    @EventHandler fun onDrop(event: PlayerDropItemEvent) = event.takeIf {
        it.player.gameMode != GameMode.CREATIVE } ?.apply { itemDrop.remove() }
}