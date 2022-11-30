package club.helix.lobby.duels.scoreboard

import club.helix.components.account.game.Duels
import club.helix.components.account.game.DuelsLevel
import club.helix.components.server.HelixServer
import club.helix.components.util.HelixAddress
import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.bukkit.kotlin.player.account
import com.google.common.base.Strings
import org.bukkit.entity.Player


class DuelsScoreboard: ScoreboardBuilder(
    "§b§lDUELS",
    "duels-lobby"
) {

    private fun progressBar(duels: Duels): String {
        val percent = (duels.exp.toDouble() / duels.expToUp).toFloat()
        val totalBars = 15
        val progressBars = (totalBars * percent).toInt()
        val separator = "∎"

        return StringBuilder().run {
            progressBars.takeIf { it > 0 }?.let {
                append("§a${Strings.repeat(separator, it)}")
            }
            (totalBars - progressBars).takeIf { it > 0 }?.let {
                append("§7${Strings.repeat(separator, it)}")
            }
            toString()
        }
    }

    override fun build(player: Player) {
        clearLines()
        addLine("")
        addLine("§fSeu nível: ", "level")
        addLine("§8[${progressBar(player.account.duels)}§8]", "progress")
        addLine("")
        addLine("§eSopa: ")
        addLine("§f Vitórias: §a", "soup-wins")
        addLine("§f Winstreak: §a", "soup-winstreak")
        addLine("§eGladiator:")
        addLine("§r §fVitórias: §a", "glad-wins")
        addLine("§r §fWinstreak: §a", "glad-winstreak")
        addLine("")
        addLine("§fOnline: §b", "total-players")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
        super.build(player)
    }


    override fun update(player: Player): Unit = player.scoreboard.let {
        val user = player.account

        user.duels.apply {
            val duelsLevel = DuelsLevel(level)
            it.getTeam("level")?.suffix = "${duelsLevel.color()}[$level${duelsLevel.symbol()}]"

            it.getTeam("progress")?.apply {
                suffix = " §7$exp/${expToUp.toString().first()}k"
            }
        }

        user.duels.soup.apply {
            it.getTeam("soup-wins")?.suffix = wins.toString()
            it.getTeam("soup-winstreak")?.suffix = winstreak.toString()
        }

        user.duels.gladiator.apply {
            it.getTeam("glad-wins")?.suffix = wins.toString()
            it.getTeam("glad-winstreak")?.suffix = winstreak.toString()
        }

        it.getTeam("total-players")?.suffix = HelixServer.networkPlayers.size.toString()
    }
}