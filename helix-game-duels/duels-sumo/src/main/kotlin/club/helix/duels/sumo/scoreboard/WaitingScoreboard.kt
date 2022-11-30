package club.helix.duels.sumo.scoreboard

import club.helix.components.util.HelixAddress
import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import club.helix.bukkit.kotlin.player.account
import club.helix.duels.sumo.SumoGame
import org.bukkit.entity.Player

class WaitingScoreboard(private val game: SumoGame): ScoreboardBuilder(
    "§b§lDUELS",
    "soup-waiting"
) {

    init {
        addLine("")
        addLine("§fModo: §a${game.mode}")
        addLine("§fJogadores: §a", "game-players")
        addLine("")
        addLine("§fAguardando...")
        addLine("")
        addLine("§fWinstreak: §7", "winstreak")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
    }

    override fun update(player: Player) = player.scoreboard.run {
        getTeam("game-players")?.suffix = "${game.playingPlayers.size}/${game.maxPlayers}"
        getTeam("winstreak")?.suffix = player.account.duels.sumo.winstreak.decimalFormat()
    }
}