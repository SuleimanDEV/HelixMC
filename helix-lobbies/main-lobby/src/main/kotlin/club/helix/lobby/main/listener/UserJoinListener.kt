package club.helix.lobby.main.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.components.account.HelixRank
import club.helix.lobby.main.MainLobby
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserJoinListener(private val plugin: MainLobby): Listener {

    @EventHandler fun onJoin(event: PlayerJoinEvent) {
        val spawnLocation = plugin.spawnLocation.clone()
        val vip = HelixRank.vip(event.player.account.mainRankLife.rank)

        event.player.teleport(spawnLocation.takeIf { !vip } ?: spawnLocation.add(0.0, 2.5, 0.0))
        plugin.scoreboard.build(event.player)
    }
}