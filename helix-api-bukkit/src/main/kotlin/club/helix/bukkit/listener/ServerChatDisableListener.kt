package club.helix.bukkit.listener

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.account
import club.helix.components.account.HelixRank
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ServerChatDisableListener(private val plugin: HelixBukkit): Listener {

    @EventHandler fun onAsyncChat(event: AsyncPlayerChatEvent) = event.takeIf {
        !plugin.serverChat.active && it.player.account.mainRankLife.rank.isLessThen(HelixRank.AJUDANTE)
    }?.apply {
        isCancelled = true
        player.sendMessage("§cO bate-papo está desativado!")
    }
}