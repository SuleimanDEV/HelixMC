package club.helix.lobby.pvp.scoreboard

import club.helix.components.server.HelixServer
import club.helix.components.util.HelixAddress
import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import club.helix.bukkit.kotlin.player.account
import org.bukkit.entity.Player

class PvPScoreboard: ScoreboardBuilder(
    "§r  §b§lPVP",
    "pvp-lobby"
) {

    init {
        addLine("")
        addLine("§eArena:")
        addLine("§f Kills: §a", "arena-kills")
        addLine("§f Killstreak: §a", "arena-killstreak")
        addLine("§eFps:")
        addLine(" §fKills: §a", "fps-kills")
        addLine(" §fKillstreak: §a", "fps-killstreak")
        addLine("")
        addLine("§fCoins: §6", "coins")
        addLine("§fOnline: §b", "online")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
    }

    override fun update(player: Player): Unit = player.scoreboard.run {
        val pvp = player.account.pvp

        pvp.arena.apply {
            getTeam("arena-kills")?.suffix = kills.decimalFormat()
            getTeam("arena-killstreak")?.suffix = killstreak.decimalFormat()
        }
        pvp.fps.apply {
            getTeam("fps-kills")?.suffix = kills.decimalFormat()
            getTeam("fps-killstreak")?.suffix = killstreak.decimalFormat()
        }

        getTeam("coins")?.suffix = pvp.coins.decimalFormat()
        getTeam("online")?.suffix = HelixServer.networkPlayers.size.decimalFormat()
    }
}