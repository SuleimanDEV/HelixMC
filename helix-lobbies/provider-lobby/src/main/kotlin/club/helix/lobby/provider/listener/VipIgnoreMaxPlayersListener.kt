package club.helix.lobby.provider.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.components.account.HelixRank
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent

class VipIgnoreMaxPlayersListener: Listener {

    @EventHandler(priority = EventPriority.LOW)
    fun onLogin(event: PlayerLoginEvent) {
        if (event.result != PlayerLoginEvent.Result.KICK_FULL) return

        if (HelixRank.vip(event.player.account.mainRankLife.rank) && event.result == PlayerLoginEvent.Result.KICK_FULL) {
            event.allow()
        }
    }
}