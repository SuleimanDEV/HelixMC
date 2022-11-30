package club.helix.duels.soup.listener

import club.helix.bukkit.kotlin.player.teleport
import club.helix.duels.api.event.UserJoinGameEvent
import club.helix.duels.soup.SoupPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class UserJoinGameListener(
    private val plugin: SoupPlugin
): Listener {

    @EventHandler fun onJoin(event: UserJoinGameEvent<*>) =
        event.player.teleport(plugin.spawnLocation)
}