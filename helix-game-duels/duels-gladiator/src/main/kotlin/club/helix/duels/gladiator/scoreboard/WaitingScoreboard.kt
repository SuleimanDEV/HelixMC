package club.helix.duels.gladiator.scoreboard

import club.helix.components.util.HelixAddress
import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import club.helix.bukkit.kotlin.player.account
import club.helix.duels.gladiator.GladiatorGame
import org.bukkit.entity.Player

class WaitingScoreboard(private val game: GladiatorGame): ScoreboardBuilder(
    "§b§lDUELS",
    "glad-waiting"
) {

    init {
        addLine("")
        addLine("§fModo: §a${game.mode}",)
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
        getTeam("winstreak")?.suffix = player.account.duels.gladiator.winstreak.decimalFormat()
    }
}