package club.helix.pvp.arena.scoreboard

import club.helix.components.util.HelixAddress
import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import club.helix.bukkit.kotlin.player.account
import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.kit.ArenaKit
import club.helix.pvp.arena.kotlin.player.arenaPlayer
import org.bukkit.entity.Player

class ArenaScoreboard(private val plugin: PvPArena): ScoreboardBuilder(
    "§b§lARENA",
    "arena"
) {

    init {
        addLine("")
        addLine("§fKills: §7", "kills")
        addLine("§fDeaths: §7", "deaths")
        addLine("")
        addLine("§fKit 1: §a", "kit1")
        addLine("§fKit 2: §a", "kit2")
        addLine("§fKillstreak: §a", "killstreak")
        addLine("")
        addLine("§fCoins: §6", "coins")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
    }

    override fun update(player: Player): Unit = player.scoreboard.run {
        player.arenaPlayer.apply {
            val primaryKit = getSelectedKit(1)?.takeIf { it != ArenaKit.NONE }?.displayName ?: "§cNenhum"
            val secondaryKit = getSelectedKit(2)?.takeIf { it != ArenaKit.NONE }?.displayName ?: "§cNenhum"
            getTeam("kit1")?.suffix = if (primaryKit.length > 16) "${primaryKit.substring(0, 13)}..." else primaryKit
            getTeam("kit2")?.suffix = if (secondaryKit.length > 16) "${secondaryKit.substring(0, 13)}..." else secondaryKit
        }

        player.account.pvp.apply {
            getTeam("kills")?.suffix = arena.kills.decimalFormat()
            getTeam("deaths")?.suffix = arena.deaths.decimalFormat()
            getTeam("killstreak")?.suffix = arena.killstreak.decimalFormat()
            getTeam("coins")?.suffix = coins.decimalFormat()
        }
    }
}