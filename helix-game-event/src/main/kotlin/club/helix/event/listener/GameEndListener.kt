package club.helix.event.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.event.event.GameEndEvent
import club.helix.event.scoreboard.EndedScoreboard
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameEndListener: Listener {

    @EventHandler fun onGameEnd(event: GameEndEvent) = event.apply {
        game.changeScoreboard(EndedScoreboard(game, winner))

        for (i in 0 until 3) {
            winner.player.world.strikeLightningEffect(winner.player.location)
        }
        Bukkit.broadcastMessage("")
        Bukkit.broadcastMessage("§a§lVENCEDOR: ${winner.player.account.tag.color}${winner.player.name}")
        Bukkit.broadcastMessage("")
    }
}