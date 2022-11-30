package club.helix.lobby.duels.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.components.account.HelixRank
import club.helix.components.util.HelixAddress
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent

class BetaVipAcessListener: Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    fun onLogin(event: PlayerLoginEvent) = event.takeIf {
        !HelixRank.vip(it.player.account.mainRankLife.rank)
    }?.apply {
        disallow(PlayerLoginEvent.Result.KICK_OTHER,
        "§cMinigame em Beta VIP. Adquira seu vip: ${HelixAddress.SHOP}")
    }
}