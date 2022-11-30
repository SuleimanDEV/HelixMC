package club.helix.bukkit.listener

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class UnknownCommandMessageListener: Listener {

    @EventHandler (ignoreCancelled = true)
    fun onPreCommand(event: PlayerCommandPreprocessEvent) = event.message.split(" ").first().apply {
        if (Bukkit.getServer().helpMap.getHelpTopic(this) == null) {
            event.isCancelled = true
            event.player.sendMessage("§cComando não encontrado.\n§cDigite /help para obter ajuda.")
        }
    }
}