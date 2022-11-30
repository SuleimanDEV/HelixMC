package club.helix.duels.soup.listener

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class DamageConfigurationListener: Listener {

    @EventHandler fun onEntityDamage(event: EntityDamageByEntityEvent) = event.takeIf {
        (it.damager as? Player)?.itemInHand?.type?.let { material ->
            material == Material.STONE_SWORD } == true
    }?.apply { damage = 3.0 }
}