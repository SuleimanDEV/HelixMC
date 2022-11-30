package club.helix.duels.lava.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockIgniteEvent

class BlockIgniteListener: Listener {

    @EventHandler fun onIgnite(event: BlockIgniteEvent) =
        run { event.isCancelled = true }
}