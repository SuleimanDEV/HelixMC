package club.helix.duels.gladiator.listener

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.duels.api.event.GameChangeStateEvent
import club.helix.duels.api.game.DuelsGameState
import club.helix.duels.gladiator.GladiatorGame
import club.helix.duels.gladiator.scoreboard.PlayingScoreboard
import club.helix.duels.gladiator.scoreboard.StartingScoreboard
import club.helix.duels.gladiator.scoreboard.WaitingScoreboard
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameSwitchScoreboardListener: Listener {

    @EventHandler fun gameStateChange(event: GameChangeStateEvent<GladiatorGame>) = event.game.apply {
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