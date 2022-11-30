package club.helix.duels.lava.listener

import club.helix.bukkit.kotlin.player.teleport
import club.helix.duels.api.event.UserJoinGameEvent
import club.helix.duels.lava.LavaPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class UserJoinGameListener(
    private val plugin: LavaPlugin
): Listener {

    @EventHandler fun onJoin(event: UserJoinGameEvent<*>) =
        event.player.teleport(plugin.spawnLocation)
}