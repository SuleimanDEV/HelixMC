package club.helix.duels.gladiator.listener

import club.helix.duels.api.DuelsAPI
import club.helix.duels.gladiator.GladiatorGame
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class ArenaBlocksListener(
    private val api: DuelsAPI<GladiatorGame>
): Listener {

    @EventHandler (ignoreCancelled = true)
    fun onBreak(event: BlockBreakEvent) = event.apply {
        val game = api.findGame(player) ?: return@apply

        if (game.isPlayingPlayer(player) && block.type == Material.GLASS) {
            event.isCancelled = true
        }
    }
}