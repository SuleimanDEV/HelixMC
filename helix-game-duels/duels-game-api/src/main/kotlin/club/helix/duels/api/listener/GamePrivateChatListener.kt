package club.helix.duels.api.listener

import club.helix.duels.api.DuelsAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class GamePrivateChatListener(
    private val api: DuelsAPI<*>
): Listener {

    @EventHandler fun onAsyncChat(event: AsyncPlayerChatEvent) = event.apply {
        val game = api.findGame(player) ?: run {
            isCancelled = true
            return@apply player.sendMessage("§cVocê não pode utilizar o chat.")
        }

        if (game.isSpectator(player)) {
            isCancelled = true
            return@apply player.sendMessage("§cEspectadores não pode utilizar o bate-papo.")
        }

        event.recipients.clear()
        event.recipients.addAll(game.allPlayers())
    }
}