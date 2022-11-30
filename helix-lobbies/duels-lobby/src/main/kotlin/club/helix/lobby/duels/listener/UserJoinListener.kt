package club.helix.lobby.duels.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.kotlin.player.location
import club.helix.components.account.HelixRank
import club.helix.lobby.duels.DuelsLobby
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserJoinListener(private val plugin: DuelsLobby): Listener {

    @EventHandler fun onJoin(event: PlayerJoinEvent) = event.player.apply {
        val spawn = location(0.41712510158413485, 65.0, -8.36413918740914, -0.15107182f, -0.60003203f)
        val vip = HelixRank.vip(event.player.account.mainRankLife.rank)

        event.player.teleport(spawn.takeIf { !vip } ?: spawn.add(0.0, 2.2, 0.0))
        plugin.scoreboard.build(event.player)
    }
}