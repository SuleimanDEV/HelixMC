package club.helix.event.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.components.account.HelixRank
import club.helix.event.EventGame
import club.helix.event.EventPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent

class UserGameWhitelistListener(private val plugin: EventPlugin): Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    fun onLogin(event: PlayerLoginEvent) = plugin.game.takeIf {
        it.state == EventGame.State.WHITELIST && !HelixRank.staff(event.player.account.mainRankLife.rank)
    }?.apply { event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cSala disponível somente para equipe do servidor.") }
}