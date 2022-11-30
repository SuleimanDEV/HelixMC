package club.helix.lobby.hg.scoreboard

import club.helix.components.server.HelixServer
import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.bukkit.kotlin.player.account
import org.bukkit.entity.Player

class HgScoreboard: ScoreboardBuilder(
    "§b§lHG",
    "hg-lobby"
) {

    init {
        addLine("")
        addLine("§eHG Mix:")
        addLine(" §fVitórias: §a", "hg-mix-wins")
        addLine(" §fKills: §a", "hg-mix-kills")
        addLine("§eLiga:")
        addLine("§r §fVitórias: §a", "hg-league-wins")
        addLine("§r §fKills: §a", "hg-league-kills")
        addLine("")
        addLine("§fCoins: §6", "coins")
        addLine("§fOnline: §b", "online")
        addLine("")
        addLine("§awww.helixmc.club")
    }

    override fun update(player: Player): Unit = player.scoreboard.let {
        val user = player.account

        user.hg.mix.apply {
            it.getTeam("hg-mix-wins")?.suffix = wins.toString()
            it.getTeam("hg-mix-kills")?.suffix = kills.toString()
        }
        user.hg.league.apply {
            it.getTeam("hg-league-wins")?.suffix = wins.toString()
            it.getTeam("hg-league-kills")?.suffix = kills.toString()
        }

        it.getTeam("coins")?.suffix = user.hg.coins.toString()
        it.getTeam("online")?.suffix = HelixServer.networkPlayers.size.toString()
    }
}