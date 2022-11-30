package club.helix.duels.sumo

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.duels.api.game.DuelsGame
import club.helix.duels.sumo.scoreboard.WaitingScoreboard

class SumoGame: DuelsGame(
    2,
    "Sumo 1v1"
) {
    override var scoreboard: ScoreboardBuilder = WaitingScoreboard(this)
}