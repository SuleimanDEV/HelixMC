package club.helix.duels.api.listener

import club.helix.duels.api.DuelsAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class DisableGameChatListener(
    private val api: DuelsAPI<*>
): Listener {

    @EventHandler fun onAsyncChat(event: AsyncPlayerChatEvent) = event.takeIf {
        api.findGame(it.player) != null
    }?.apply {
        isCancelled = true
        player.sendMessage("§cO bate-papo deste jogo é desativado.")
    }
}