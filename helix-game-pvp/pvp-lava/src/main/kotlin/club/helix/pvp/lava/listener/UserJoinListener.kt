package club.helix.pvp.lava.listener

import club.helix.pvp.lava.PvPLava
import club.helix.pvp.lava.kotlin.player.registerLavaPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserJoinListener(private val plugin: PvPLava): Listener {

    @EventHandler fun onJoin(event: PlayerJoinEvent) = event.player.run {
        event.joinMessage = null
        registerLavaPlayer()
        plugin.serverSpawn.send(this)
        plugin.scoreboard.build(this)
    }
}