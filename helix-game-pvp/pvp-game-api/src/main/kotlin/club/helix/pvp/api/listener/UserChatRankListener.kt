package club.helix.pvp.api.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.pvp.api.GameAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class UserChatRankListener(private val gameAPI: GameAPI): Listener {

    @EventHandler (ignoreCancelled = true)
    fun onAsyncChat(event: AsyncPlayerChatEvent) = event.apply {
        format = "${player.account.pvp.displayRank()} §r$format"
    }
}