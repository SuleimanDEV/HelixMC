package club.helix.hg.scoreboard

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.components.util.HelixAddress
import club.helix.hg.HgGame
import club.helix.hg.player.GamePlayer

class EndedScoreboard(
    game: HgGame,
    winner: GamePlayer,
): ScoreboardBuilder(
    "§b§lHG", "hg-ended"
) {

    init {
        val winnerStr = winner.player.account.tag.color + winner.player.name

        addLine("")
        addLine("§fVencedor: ")
        addLine(winnerStr.takeIf { it.length <= 17 }
            ?: winnerStr.substring(0, 17))
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
    }
}