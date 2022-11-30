package club.helix.lobby.hg.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.kotlin.player.location
import club.helix.components.account.HelixRank
import club.helix.lobby.hg.HgLobby
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserJoinListener(private val plugin: HgLobby): Listener {

    @EventHandler fun onJoin(event: PlayerJoinEvent) {
        val spawn = event.player.location(0.5021037318526171, 67.0, -9.41985093706874, 7.879138E-5f, -0.1500037f)
        val vip = HelixRank.vip(event.player.account.mainRankLife.rank)

        event.player.teleport(spawn.takeIf { !vip } ?: spawn.add(0.0, 2.2, 0.0))
        plugin.scoreboard.build(event.player)
    }
}