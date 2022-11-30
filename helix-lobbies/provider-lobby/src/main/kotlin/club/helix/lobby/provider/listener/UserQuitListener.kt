package club.helix.lobby.provider.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class UserQuitListener: Listener {

    @EventHandler fun onQuit(event: PlayerQuitEvent) =
        run { event.quitMessage = null }
}