package club.helix.event.scoreboard

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.components.util.HelixAddress
import club.helix.event.EventGame
import org.bukkit.entity.Player

class WhitelistScoreboard(
    private val game: EventGame
): ScoreboardBuilder(
    "§b§lEVENTO", "event-whitelist"
) {

    init {
        addLine("")
        addLine("§eA sala está no modo")
        addLine("§ede configuração.")
        addLine("")
        addLine("§fPremiação:")
        addLine("§e", "reward")
        addLine("")
        addLine("§fEvento: §a", "event")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
    }

    override fun update(player: Player): Unit = player.scoreboard.run {
        game.getConfig<String>("reward")?.let { reward ->
            getTeam("reward")?.suffix = reward.takeIf { it.length <= 15 } ?: reward.substring(0, 15)
        }
        game.getConfig<String>("name")?.let { name ->
            getTeam("event")?.suffix = name.takeIf { it.length <= 15 } ?: name.substring(0, 15)
        }
    }
}