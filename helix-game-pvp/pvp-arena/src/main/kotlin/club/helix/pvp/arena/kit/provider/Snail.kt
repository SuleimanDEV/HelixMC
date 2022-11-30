package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandler
import club.helix.pvp.arena.kotlin.player.allowedPvP
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class Snail: KitHandler() {

    @EventHandler fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        it.entity is Player && it.damager is Player && (it.entity as Player).allowedPvP && valid(it.damager as Player)
    }?.takeIf { Random().nextInt(100) <= 38 }?.apply {
        (entity as Player).addPotionEffect(PotionEffect(PotionEffectType.SLOW, 6 * 20, 0))
    }
}