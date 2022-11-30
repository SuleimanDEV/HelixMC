package club.helix.lobby.login.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinQuitMessageListener: Listener {

    @EventHandler fun onJoin(event: PlayerJoinEvent) =
        run { event.joinMessage = null }

    @EventHandler fun onQuit(event: PlayerQuitEvent) =
        run { event.quitMessage = null }
}