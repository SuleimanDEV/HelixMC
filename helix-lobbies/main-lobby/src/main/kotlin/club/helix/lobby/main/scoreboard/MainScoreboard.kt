package club.helix.lobby.main.scoreboard

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.components.server.HelixServer
import club.helix.components.util.HelixAddress
import org.bukkit.entity.Player

class MainScoreboard: ScoreboardBuilder(
    "§b§lHELIX",
    "main-lobby"
) {

    init {
        addLine("")
        addLine("§fRank: ", "rank")
        addLine("")
        addLine("§fLobby: §7#", "lobby")
        addLine("§fOnline: §a", "total-players")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}",)
    }

    override fun update(player: Player): Unit = player.scoreboard.let {
        val user = player.account

        it.getTeam("rank")?.suffix = user.mainRankLife.rank.coloredName
        it.getTeam("lobby")?.suffix = HelixBukkit.instance.currentServer.id.toString()
        it.getTeam("total-players")?.suffix = HelixServer.networkPlayers.size.toString()
    }
}