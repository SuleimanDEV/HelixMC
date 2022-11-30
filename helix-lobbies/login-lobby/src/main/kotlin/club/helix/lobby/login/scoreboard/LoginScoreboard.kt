package club.helix.lobby.login.scoreboard

import club.helix.components.util.HelixAddress
import club.helix.bukkit.builder.ScoreboardBuilder
import org.bukkit.entity.Player

class LoginScoreboard: ScoreboardBuilder(
    "§b§lHELIX",
    "loginsb"
) {
    init {
        addLine("")
        addLine("§7Bem-vindo de volta,")
        addLine("§f", "player-name")
        addLine("")
        addLine("§eFaça login para ter")
        addLine("§eacesso ao servidor.")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
    }

    override fun update(player: Player): Unit = player.scoreboard.let {
        it.getTeam("player-name")?.suffix = if (player.name.length > 16) player.name.substring(0, 16) else player.name
    }
}