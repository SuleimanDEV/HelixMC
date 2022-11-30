package club.helix.bukkit.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.components.account.HelixRank
import club.helix.components.util.HelixTimeData
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.concurrent.TimeUnit

class ChatCooldownListener: Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onAsyncChat(event: AsyncPlayerChatEvent) = event.takeIf {
        it.player.account.mainRankLife.rank.let { rank ->
            !HelixRank.vip(rank) && !HelixRank.staff(rank) }
    }?.apply {
        if (HelixTimeData.getOrCreate(player.name, "chat", 3, TimeUnit.SECONDS)) {
            player.sendMessage("§cAguarde ${HelixTimeData.getTimeFormatted(player.name, "chat")} para utilizar o bate-papo novamente.")
            isCancelled = true
        }
    }
}