package club.helix.duels.lava

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.duels.api.game.DuelsGame
import club.helix.duels.lava.scoreboard.WaitingScoreboard

class LavaGame: DuelsGame(
    2,
    "Lava 1v1"
) {
    override var scoreboard: ScoreboardBuilder = WaitingScoreboard(this)
}