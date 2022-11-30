package club.helix.lobby.duels.listener

import club.helix.components.account.game.DuelsLevel
import club.helix.bukkit.kotlin.player.account
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class DuelsChatListener: Listener {

    @EventHandler (priority = EventPriority.MONITOR)
    fun onAsyncChat(event: AsyncPlayerChatEvent) = event.apply {
        val level = player.account.duels.level
        val duelsLevel = DuelsLevel(level)
        event.format = "${duelsLevel.color()}[$level${duelsLevel.symbol()}] §r$format"
    }
}