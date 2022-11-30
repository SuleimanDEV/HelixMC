package club.helix.duels.uhc.listener

import club.helix.duels.api.DuelsAPI
import club.helix.duels.uhc.UHCGame
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

class ArenaBlocksListener(
    private val api: DuelsAPI<UHCGame>
): Listener {

    @EventHandler (ignoreCancelled = true)
    fun onPlace(event: BlockPlaceEvent) = event.apply {
        api.findGame(player)?.placedBlocks?.add(block)
    }

    @EventHandler (ignoreCancelled = true)
    fun onBreak(event: BlockBreakEvent) = event.apply {
        val game = api.findGame(player) ?: return@apply

        if (!game.isPlayingPlayer(player) || !game.placedBlocks.contains(block)) {
            event.isCancelled = true
        }
    }
}