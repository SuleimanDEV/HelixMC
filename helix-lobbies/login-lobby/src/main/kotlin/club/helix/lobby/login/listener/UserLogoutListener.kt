package club.helix.lobby.login.listener

import club.helix.lobby.login.LoginLobby
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class UserLogoutListener(private val plugin: LoginLobby): Listener {

    @EventHandler fun onQuit(event: PlayerQuitEvent) = event.player.name.run {
        plugin.removeAuthenticate(this)
        plugin.loggingPlayers.remove(this)
    }

}