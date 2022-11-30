package club.helix.lobby.login.scoreboard

import club.helix.components.util.HelixAddress
import club.helix.bukkit.builder.ScoreboardBuilder

class RegisterScoreboard: ScoreboardBuilder(
    "§b§lHELIX",
    "registersb"
) {
    init {
        addLine("")
        addLine("§7Este é o início de sua")
        addLine("§7jornada no Helix.")
        addLine("")
        addLine("§eCrie uma conta para")
        addLine("§eaproveitar o servidor.")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
    }
}