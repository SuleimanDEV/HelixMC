package club.helix.duels.sumo.listener

import club.helix.duels.sumo.SumoPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class PlayerWaterGroundListener(
    private val plugin: SumoPlugin
): Listener {

    @EventHandler fun onMove(event: PlayerMoveEvent) = event.takeIf {
        it.player.location.block.type.toString().contains("WATER")
                && plugin.duelsAPI.findGame(it.player)?.let { game -> !game.isSpectator(it.player) } == true
    }?.player?.apply {
        val game = plugin.duelsAPI.findGame(this)!!
        val loser = game.getPlayingPlayer(this)
            ?: throw NullPointerException("invalid loser")
        game.end(loser)
    }
}