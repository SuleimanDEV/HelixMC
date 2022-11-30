package club.helix.duels.api.listener

import club.helix.duels.api.DuelsAPI
import club.helix.duels.api.game.DuelsGameState
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class PreGameDamageListener(
    private val api: DuelsAPI<*>
): Listener {

    @EventHandler fun onEntityDamage(event: EntityDamageEvent) = event.takeIf {
        it.entity is Player
    }?.apply {
        val game = api.findGame(entity as Player)
            ?: throw NullPointerException("invalid game")

        if (game.state != DuelsGameState.PLAYING) {
            event.isCancelled = true
        }
    }

    @EventHandler fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        it.entity is Player
    }?.apply {
        val game = api.findGame(entity as Player)
            ?: throw NullPointerException("invalid game")

        if (game.state != DuelsGameState.PLAYING) {
            event.isCancelled = true
        }
    }
}