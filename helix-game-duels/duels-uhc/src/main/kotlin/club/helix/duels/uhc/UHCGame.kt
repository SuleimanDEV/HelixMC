package club.helix.duels.uhc

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.duels.api.game.DuelsGame
import club.helix.duels.uhc.scoreboard.WaitingScoreboard
import org.bukkit.block.Block

class UHCGame: DuelsGame(
    2,
    "UHC 1v1"
) {
    override var scoreboard: ScoreboardBuilder = WaitingScoreboard(this)
    val mapGenerator = UHCMapGenerator(this)
    val placedBlocks = mutableListOf<Block>()
}