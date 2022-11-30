package club.helix.event.scoreboard

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.components.util.HelixAddress
import club.helix.event.EventGame
import club.helix.event.player.GamePlayerType
import org.bukkit.entity.Player

class PlayingScoreboard(
    private val game: EventGame
): ScoreboardBuilder(
    "§b§lEVENTO", "event-play"
) {

    init {
        addLine("")
        addLine("§fTempo: §7", "time")
        addLine("§fJogadores: §a", "players")
        addLine("")
        addLine("§fPremiação:")
        addLine("§e", "reward")
        addLine("")
        addLine("§fEvento: §a", "event")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
    }

    override fun update(player: Player): Unit = player.scoreboard.run {
        getTeam("time")?.suffix = game.getTimeFormatted()

        val maxPlayers = game.getConfig<Int>("max-players") ?: 100
        getTeam("players")?.suffix = "${game.players.filter { it.type == GamePlayerType.PLAYING }.size}/$maxPlayers"

        game.getConfig<String>("reward")?.let { reward ->
            getTeam("reward")?.suffix = reward.takeIf { it.length <= 15 } ?: reward.substring(0, 15)
        }
        game.getConfig<String>("name")?.let { name ->
            getTeam("event")?.suffix = name.takeIf { it.length <= 15 } ?: name.substring(0, 15)
        }
    }

}