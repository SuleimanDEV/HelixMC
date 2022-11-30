package club.helix.duels.gladiator

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.duels.api.game.DuelsGame
import club.helix.duels.gladiator.scoreboard.WaitingScoreboard

class GladiatorGame: DuelsGame(
    2,
    "Gladiator 1v1"
) {
    override var scoreboard: ScoreboardBuilder = WaitingScoreboard(this)
    val mapGenerator = MapGenerator(this)
}