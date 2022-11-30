package club.helix.event.scoreboard

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.components.util.HelixAddress
import club.helix.event.EventGame
import club.helix.event.player.GamePlayer

class EndedScoreboard(
    game: EventGame,
    winner: GamePlayer,
): ScoreboardBuilder(
    "§b§lEVENTO", "event-ended"
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