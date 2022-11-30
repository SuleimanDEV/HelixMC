package club.helix.pvp.fps.listener

import club.helix.pvp.fps.PvPFps
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserJoinListener(private val plugin: PvPFps): Listener {

    @EventHandler fun onJoin(event: PlayerJoinEvent) = event.player.apply {
        plugin.serverSpawnHandle.send(event.player)
        plugin.scoreboard.build(this)
    }
}