package club.helix.bukkit.listener

import club.helix.components.util.CensureKeyWords
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatCensureKeyWordListener: Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onAsyncChat(event: AsyncPlayerChatEvent) = event.apply {
        message = CensureKeyWords.matcher(message)
    }
}