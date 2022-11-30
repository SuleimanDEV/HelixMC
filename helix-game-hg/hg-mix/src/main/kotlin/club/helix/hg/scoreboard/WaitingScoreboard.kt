package club.helix.hg.scoreboard

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.components.util.HelixAddress
import club.helix.hg.HgGame
import org.bukkit.entity.Player

class WaitingScoreboard(
    private val game: HgGame
): ScoreboardBuilder(
    "§b§lHG", "hg-wait"
) {

    init {
        addLine("")
        addLine("§fIniciando em: §7", "time")
        addLine("§fJogadores: §a", "players")
        addLine("")
        addLine("§fSala: §a#?")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
    }

    override fun update(player: Player): Unit = player.scoreboard.run {
        getTeam("time")?.suffix = game.getTimeFormatted()
    }
}