package club.helix.pvp.fps.scoreboard

import club.helix.components.util.HelixAddress
import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import club.helix.bukkit.kotlin.player.account
import org.bukkit.entity.Player

class FpsScoreboard: ScoreboardBuilder("§b§lFPS", "pvp-fps") {

    init {
        addLine("")
        addLine("§fKills: §7", "kills")
        addLine("§fDeaths: §7", "deaths")
        addLine("§fKillstreak: §a", "killstreak")
        addLine("")
        addLine("§fCoins: §6", "coins")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
    }

    override fun update(player: Player): Unit = player.scoreboard.let {
        val user = player.account

        user.pvp.fps.apply {
            it.getTeam("kills")?.suffix = kills.toString()
            it.getTeam("deaths")?.suffix = deaths.toString()
            it.getTeam("killstreak")?.suffix = killstreak.toString()
        }
        it.getTeam("coins")?.suffix = user.pvp.coins.decimalFormat()
    }
}