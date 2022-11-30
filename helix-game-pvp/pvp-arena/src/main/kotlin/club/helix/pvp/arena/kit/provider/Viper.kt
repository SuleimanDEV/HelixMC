package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandler
import club.helix.pvp.arena.kotlin.player.allowedPvP
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class Viper: KitHandler() {

    @EventHandler
    fun onDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        it.entity is Player && it.damager is Player && (it.entity as Player).allowedPvP && valid(it.damager as Player)
    }?.takeIf { Random().nextInt(100) <= 43 }?.run {
        (entity as Player).addPotionEffect(PotionEffect(PotionEffectType.POISON, 6 * 20, 1))
    }
}