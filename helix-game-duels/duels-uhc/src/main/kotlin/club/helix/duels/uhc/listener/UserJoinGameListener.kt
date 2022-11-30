package club.helix.duels.uhc.listener

import club.helix.duels.api.event.UserJoinGameEvent
import club.helix.duels.uhc.UHCGame
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class UserJoinGameListener: Listener {

    @EventHandler fun onJoin(event: UserJoinGameEvent<UHCGame>) =
        event.player.teleport(event.game.mapGenerator.location)
}