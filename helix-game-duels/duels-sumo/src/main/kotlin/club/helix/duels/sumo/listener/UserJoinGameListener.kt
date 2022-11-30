package club.helix.duels.sumo.listener

import club.helix.bukkit.kotlin.player.teleport
import club.helix.duels.api.event.UserJoinGameEvent
import club.helix.duels.sumo.SumoPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class UserJoinGameListener(
    private val plugin: SumoPlugin
): Listener {

    @EventHandler fun onJoin(event: UserJoinGameEvent<*>) =
        event.player.teleport(plugin.spawnLocation)
}