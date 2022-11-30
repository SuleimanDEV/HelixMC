package club.helix.lobby.provider.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent

class ServerPingListener: Listener {

    @EventHandler fun onPing(event: ServerListPingEvent) = run { event.maxPlayers = 75 }
}