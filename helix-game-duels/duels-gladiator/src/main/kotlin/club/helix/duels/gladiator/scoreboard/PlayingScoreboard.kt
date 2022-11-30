package club.helix.duels.gladiator.scoreboard

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import club.helix.components.util.HelixAddress
import club.helix.duels.gladiator.GladiatorGame
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

class PlayingScoreboard(private val game: GladiatorGame): ScoreboardBuilder(
    "§b§lDUELS",
    "glad-starting"
) {

    override fun build(player: Player) {
        clearLines()
        addLine("")
        addLine("§fModo: §a${game.mode}",)
        addLine("§fTempo: §a", "playing-time")
        addLine("")
        if (game.playingPlayers.isNotEmpty()) {
            val gamePlayer = game.playingPlayers[0].run {
                "${teamColor.color}${this.player.name}"
            }
            addLine("${gamePlayer.takeIf { it.length <= 16 } 
                ?: "${gamePlayer.substring(0, 13)}..."}: ", "game-p1")
        }
        if (game.playingPlayers.size >= 2) {
            val gamePlayer = game.playingPlayers[1].run {
                "${teamColor.color}${this.player.name}"
            }
            addLine("${gamePlayer.takeIf { it.length <= 16 }
                ?: "${gamePlayer.substring(0, 13)}..."}: ", "game-p2")
        }
        addLine("")
        addLine("§fWinstreak: §7", "winstreak")
        addLine("")
        addLine("§awww.${HelixAddress.DOMAIN}")
        super.build(player)
    }

    override fun update(player: Player): Unit = player.scoreboard.run {
        getTeam("playing-time")?.suffix = game.geTimeFormatted()
        getTeam("winstreak")?.suffix = player.account.duels.gladiator.winstreak.decimalFormat()

        getTeam("game-p1")?.let { team ->
            val ping = (game.playingPlayers.first().player as CraftPlayer).handle.ping
            team.suffix = "§7${ping}ms"
        }

        getTeam("game-p2")?.let { team ->
            val ping = (game.playingPlayers[1].player as CraftPlayer).handle.ping
            team.suffix = "§7${ping}ms"
        }
    }
}