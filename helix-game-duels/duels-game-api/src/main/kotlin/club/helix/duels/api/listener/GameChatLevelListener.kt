package club.helix.duels.api.listener

import club.helix.components.account.game.DuelsLevel
import club.helix.bukkit.kotlin.player.account
import club.helix.duels.api.DuelsAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class GameChatLevelListener(
    private val api: DuelsAPI<*>
): Listener {

    @EventHandler (ignoreCancelled = true)
    fun onAsyncChat(event: AsyncPlayerChatEvent) = event.apply {
        val level = player.account.duels.level
        val duelsLevel = DuelsLevel(level)
        format = "${duelsLevel.color()}[$level${duelsLevel.symbol()}] §r$format"
    }
}