package club.helix.duels.api.listener

import club.helix.duels.api.DuelsAPI
import club.helix.duels.api.game.DuelsGameState
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class UserDeathListener(
    private val api: DuelsAPI<*>
): Listener {

    @EventHandler fun onUserDeath(event: PlayerDeathEvent) = api.findGame(event.entity)?.apply {
        event.apply {
            drops.clear()
            deathMessage = null
            entity.apply {
                spigot().respawn()
                fireTicks = 0
            }
        }

        if (state == DuelsGameState.PLAYING) {
            val loser = getPlayingPlayer(event.entity) ?: throw NullPointerException("invalid loser")
            end(loser)
        }
    }
}