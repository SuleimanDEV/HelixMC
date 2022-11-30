package club.helix.duels.api.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.duels.api.DuelsAPI
import club.helix.duels.api.game.DuelsGameState
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class UserGameLeaveListener(
    private val api: DuelsAPI<*>
): Listener {

    @EventHandler fun onQuit(event: PlayerQuitEvent) {
        event.quitMessage = null
        val game = api.findGame(event.player) ?: return

        game.getSpectatorPlayer(event.player)?.run {
            return game.removePlayer(player)
        }

        val loser = game.getPlayingPlayer(event.player)
            ?: throw NullPointerException("invalid loser")
        game.removePlayer(loser.player)

        if (game.playingPlayers.isNotEmpty() && game.state == DuelsGameState.STARTING) {
            val user = event.player.account
            game.broadcast("${user.tag.color}${user.name} §csaiu da sala.")
        }

        if (game.playingPlayers.isEmpty()) {
            return api.deleteGame(game)
        }

        if (game.state == DuelsGameState.PLAYING) {
            game.end(loser)
        }
    }
}