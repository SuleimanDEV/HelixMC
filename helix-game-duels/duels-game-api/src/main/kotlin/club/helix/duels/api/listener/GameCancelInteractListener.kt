package club.helix.duels.api.listener

import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class GameCancelInteractListener: Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.player.gameMode != GameMode.CREATIVE
    }?.apply { isCancelled = true }
}