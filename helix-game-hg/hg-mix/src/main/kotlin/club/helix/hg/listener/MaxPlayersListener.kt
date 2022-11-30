package club.helix.hg.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.components.account.HelixRank
import club.helix.hg.HgPlugin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent

class MaxPlayersListener(private val plugin: HgPlugin): Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    fun onKick(event: PlayerLoginEvent) = event.apply {
        val maxPlayers = plugin.game.maxPlayers
        val online = Bukkit.getOnlinePlayers().size
        val vip = HelixRank.vip(player.account.mainRankLife.rank)

        if (online >= maxPlayers && !vip) {
            disallow(PlayerLoginEvent.Result.KICK_FULL, "§cAdquira vip para entrar.")
        }else {
            allow()
        }
    }
}