package club.helix.bukkit.listener

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.account
import club.helix.components.account.HelixRank
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class PlayerChatListener(private val apiBukkit: HelixBukkit): Listener {

    @EventHandler (ignoreCancelled = true)
    fun onAsyncChat(event: AsyncPlayerChatEvent) = event.apply {
        try {
            val user = player.account
            if (!HelixRank.staff(user.tag)) {
                event.recipients.removeIf { it != player && !it.account.preferences.chat.receiveChatMessage }
            }

            val medal = user.medal.let { medal -> medal.color + (medal.icon.takeIf { it.isNotEmpty() }?.let { "$it " } ?: "") }
            val clan = apiBukkit.components.clanManager.getClanByMember(player.name)?.run {
                val tagColor = ChatColor.getByChar(tagColorCode.toString())
                "$tagColor[$tag] "
            } ?: ""
            var message = event.message.replace("%",  "%%")
            if (player.hasPermission("helix.chat.color")) {
                message = ChatColor.translateAlternateColorCodes('&', message)
            }

            format = "$medal$clan${user.tag.prefix}${player.name}: §f$message".trim()
        }catch (error: Exception) {
            error.printStackTrace()
            player.sendMessage("§cOcorreu um erro ao enviar esta mensagem.")
        }
    }
}