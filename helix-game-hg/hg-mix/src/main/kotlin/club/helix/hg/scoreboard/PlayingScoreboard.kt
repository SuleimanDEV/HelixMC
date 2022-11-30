package club.helix.hg.scoreboard

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.components.util.HelixAddress
import club.helix.hg.HgGame
import org.bukkit.entity.Player

class PlayingScoreboard(
    private val game: HgGame
): ScoreboardBuilder(
    "§b§lHG", "hg-play"
) {

    init {
        addLine("")
        addLine("§fTempo: §7", "time")
        addLine("§fJogadores: §a", "players")
        addLine("")
        addLine("§fKit: X")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
    }

    override fun update(player: Player): Unit = player.scoreboard.run {
        getTeam("time")?.suffix = game.getTimeFormatted()
    }

}