package club.helix.duels.soup

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.duels.api.game.DuelsGame
import club.helix.duels.soup.scoreboard.WaitingScoreboard

class SoupGame: DuelsGame(
    2,
    "Sopa 1v1"
) {
    override var scoreboard: ScoreboardBuilder = WaitingScoreboard(this)
}