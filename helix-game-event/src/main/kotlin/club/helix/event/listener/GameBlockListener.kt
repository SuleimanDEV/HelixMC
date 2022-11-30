package club.helix.event.listener

import club.helix.event.EventPlugin
import club.helix.event.player.GamePlayerType
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

class GameBlockListener(private val plugin: EventPlugin): Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onBlockPlace(event: BlockPlaceEvent) = event.takeIf {
        plugin.game.hasPlayer(it.player, GamePlayerType.PLAYING) &&
                it.player.gameMode != GameMode.CREATIVE
    }?.apply { plugin.game.blocks.add(block) }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onBlockBreak(event: BlockBreakEvent) = event.takeIf {
        plugin.game.blocks.contains(it.block)
    }?.apply { plugin.game.blocks.remove(block) }
}