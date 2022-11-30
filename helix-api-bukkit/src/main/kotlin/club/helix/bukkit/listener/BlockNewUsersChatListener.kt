package club.helix.bukkit.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.components.util.HelixTimeFormat
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.concurrent.TimeUnit

class BlockNewUsersChatListener: Listener {

    private fun isNewUser(player: Player) = TimeUnit.MILLISECONDS
        .toMinutes(System.currentTimeMillis() - player.account.login.firstLogin) <= 7

    private fun sendRemaingTimeMessage(player: Player) {
        val remaingTime = HelixTimeFormat.format(
            System.currentTimeMillis() - player.account.login.firstLogin)
        player.sendMessage("§cVocê é novo por aqui, aguarde $remaingTime para utilizar o bate-papo.")
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onAsyncChat(event: AsyncPlayerChatEvent) = event.takeIf {
        isNewUser(it.player)
    }?.apply {
        isCancelled = true
        sendRemaingTimeMessage(player)
    }
}