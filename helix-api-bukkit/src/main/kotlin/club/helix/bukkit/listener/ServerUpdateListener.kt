package club.helix.bukkit.listener

import club.helix.bukkit.HelixBukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ServerUpdateListener(private val apiBukkit: HelixBukkit): Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) = HelixBukkit.instance.currentServer.apply {
        onlinePlayers.add(event.player.name)
        apiBukkit.components.callUpdateServer(this)
    }

    @EventHandler fun onQuit(event: PlayerQuitEvent) = HelixBukkit.instance.currentServer.apply {
        onlinePlayers.remove(event.player.name)
        apiBukkit.components.callUpdateServer(this)
    }
}