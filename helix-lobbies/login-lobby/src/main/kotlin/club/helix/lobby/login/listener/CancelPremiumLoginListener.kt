package club.helix.lobby.login.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.components.account.HelixUserLogin
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent

class CancelPremiumLoginListener: Listener {

    @EventHandler (priority = EventPriority.MONITOR)
    fun onLogin(event: PlayerLoginEvent) = event.player.account.takeIf {
        it.login.type == HelixUserLogin.Type.PREMIUM
    }?.apply { event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
        "§cApenas jogadores piratas podem acessar o servidor de login.") }
}