package club.helix.duels.soup.listener

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.duels.api.event.GameChangeStateEvent
import club.helix.duels.api.game.DuelsGameState
import club.helix.duels.soup.SoupGame
import club.helix.duels.soup.scoreboard.PlayingScoreboard
import club.helix.duels.soup.scoreboard.StartingScoreboard
import club.helix.duels.soup.scoreboard.WaitingScoreboard
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameSwitchScoreboardListener: Listener {

    @EventHandler fun gameStateChange(event: GameChangeStateEvent<SoupGame>) = event.game.apply {
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