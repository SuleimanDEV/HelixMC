package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandler
import club.helix.pvp.arena.kotlin.player.allowedPvP
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class Boxer: KitHandler() {

    @EventHandler (ignoreCancelled = true)
    fun onBoxerReceiveDamage(event: EntityDamageByEntityEvent) = event.takeIf {
        (it.damager as? Player)?.allowedPvP == true &&
                (it.entity as? Player)?.let { entity ->  valid(entity) && entity.itemInHand.type == Material.AIR} == true
    }?.apply { damage -= 1 }
}