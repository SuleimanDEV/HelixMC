package club.helix.pvp.lava.scoreboard

import club.helix.components.util.HelixAddress
import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import club.helix.bukkit.kotlin.player.account
import club.helix.pvp.lava.kotlin.player.lavaPlayer
import org.bukkit.entity.Player

class LavaScoreboard: ScoreboardBuilder(
    "§b§lLAVA", "lava"
) {

    init {
        addLine("")
        addLine("§fDano: §c", "damage")
        addLine("§fCoins: §6", "coins")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
    }

    override fun update(player: Player): Unit = player.scoreboard.run {
        getTeam("damage")?.suffix = "${(player.lavaPlayer.lavaDamage / 2.0)} ❤"
        getTeam("coins")?.suffix = player.account.pvp.coins.decimalFormat()
    }
}