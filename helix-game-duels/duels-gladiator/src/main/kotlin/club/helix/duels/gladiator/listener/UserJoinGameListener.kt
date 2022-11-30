package club.helix.duels.gladiator.listener

import club.helix.duels.api.event.UserJoinGameEvent
import club.helix.duels.gladiator.GladiatorGame
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class UserJoinGameListener: Listener {

    @EventHandler fun onJoin(event: UserJoinGameEvent<GladiatorGame>) =
        event.player.teleport(event.game.mapGenerator.location)
}