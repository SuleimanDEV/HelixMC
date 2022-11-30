package club.helix.event.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.components.account.HelixRank
import club.helix.event.EventPlugin
import club.helix.event.player.GamePlayerType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerPickupItemEvent

class SpectatorBlockEventListener(private val plugin: EventPlugin): Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onAsyncChat(event: AsyncPlayerChatEvent) = event.takeIf {
        plugin.game.hasPlayer(it.player, GamePlayerType.SPECTATOR) &&
                !HelixRank.staff(it.player.account.mainRankLife.rank)
    }?.apply {
        isCancelled = true
        player.sendMessage("§cEspectadores não podem utilizar o bate-papo.")
    }

    @EventHandler fun onPickupItem(event: PlayerPickupItemEvent) = event.takeIf {
        plugin.game.hasPlayer(it.player, GamePlayerType.SPECTATOR)
    }?.apply { isCancelled = true }
}