package club.helix.pvp.lava.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockIgniteEvent

class BlockIgniteListener: Listener {

    @EventHandler fun onBlockIgnite(event: BlockIgniteEvent) = event.takeIf {
        it.cause == BlockIgniteEvent.IgniteCause.LAVA ||
                it.cause == BlockIgniteEvent.IgniteCause.SPREAD
    }?.apply { event.isCancelled = true }
}