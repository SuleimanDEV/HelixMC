package club.helix.duels.lava.listener

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.duels.api.event.GameChangeStateEvent
import club.helix.duels.api.game.DuelsGameState
import club.helix.duels.lava.LavaGame
import club.helix.duels.lava.scoreboard.PlayingScoreboard
import club.helix.duels.lava.scoreboard.StartingScoreboard
import club.helix.duels.lava.scoreboard.WaitingScoreboard
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameSwitchScoreboardListener: Listener {

    @EventHandler fun gameStateChange(event: GameChangeStateEvent<LavaGame>) = event.game.apply {
        val scoreboard: ScoreboardBuilder? = when (event.newState) {
            DuelsGameState.WAITING ->  WaitingScoreboard(this)
            DuelsGameState.STARTING -> StartingScoreboard(this)
            DuelsGameState.PLAYING, DuelsGameState.ENDED -> PlayingScoreboard(this)
            else -> null
        }

        if (scoreboard != null) {
            this.scoreboard = scoreboard
            allPlayers().forEach(scoreboard::build)
        }
    }
}