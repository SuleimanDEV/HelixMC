package club.helix.lobby.login.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.components.account.HelixRank
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class CancelLoginEventListener: Listener {

    @EventHandler fun onChat(event: AsyncPlayerChatEvent) = event.player.takeIf {
        it.account.mainRankLife.rank != HelixRank.DIRETOR
    }?.run { event.isCancelled = true }
}