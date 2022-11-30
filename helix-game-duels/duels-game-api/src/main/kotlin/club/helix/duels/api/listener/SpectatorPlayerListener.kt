package club.helix.duels.api.listener

import club.helix.duels.api.DuelsAPI
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class SpectatorPlayerListener(
    private val api: DuelsAPI<*>
): Listener {

    @EventHandler fun onEntityDamage(event: EntityDamageEvent) = event.takeIf {
        (event.entity as? Player)?.let { player ->
            api.findGame(player)?.isSpectator(player) == true
        } == true
    }?.apply { isCancelled = true }

    @EventHandler fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        (event.damager as? Player)?.let { player ->
            api.findGame(player)?.isSpectator(player) == true
        } == true
    }?.apply { isCancelled = true }
}