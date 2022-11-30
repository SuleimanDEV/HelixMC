package club.helix.duels.sumo.listener

import club.helix.duels.sumo.SumoPlugin
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class DamageConfigurationListener(
    private val plugin: SumoPlugin
): Listener {

    @EventHandler (ignoreCancelled = true)
    fun onEntityDamage(event: EntityDamageByEntityEvent) = event.takeIf {
        (it.damager as? Player)?.let {
                damager -> plugin.duelsAPI.findGame(damager) != null
        } == true
    }?.apply { damage = 0.0 }
}