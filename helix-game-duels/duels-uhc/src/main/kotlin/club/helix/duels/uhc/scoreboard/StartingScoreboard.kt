package club.helix.duels.uhc.scoreboard

import club.helix.components.util.HelixAddress
import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import club.helix.bukkit.kotlin.player.account
import club.helix.duels.uhc.UHCGame
import org.bukkit.entity.Player

class StartingScoreboard(private val game: UHCGame): ScoreboardBuilder(
    "§b§lDUELS",
    "uhc-starting"
) {

    init {
        addLine("")
        addLine("§fModo: §a${game.mode}",)
        addLine("§fJogadores: §a", "game-players")
        addLine("")
        addLine("§fIniciando em §a", "starting-time")
        addLine("")
        addLine("§fWinstreak: §7", "winstreak")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
    }

    override fun update(player: Player) = player.scoreboard.run {
        getTeam("game-players")?.suffix = "${game.playingPlayers.size}/${game.maxPlayers}"
        getTeam("starting-time")?.suffix = "${game.time}s"
        getTeam("winstreak")?.suffix = player.account.duels.uhc.winstreak.decimalFormat()
    }
}