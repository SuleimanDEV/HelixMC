package club.helix.lobby.pvp.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.components.account.HelixRank
import club.helix.lobby.pvp.PvPLobby
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserJoinListener(private val plugin: PvPLobby): Listener {

    @EventHandler fun onJoin(event: PlayerJoinEvent) {
        val spawn = plugin.spawnLocation.clone()
        val vip = HelixRank.vip(event.player.account.mainRankLife.rank)

        event.player.teleport(spawn.takeIf { !vip } ?: spawn.add(0.0, 2.2, 0.0))
        plugin.scoreboard.build(event.player)
    }
}