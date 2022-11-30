package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandler
import club.helix.pvp.arena.kit.provider.event.PlayerStomperEvent
import club.helix.pvp.arena.kotlin.player.allowedPvP
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent

class Stomper: KitHandler() {

    @EventHandler (ignoreCancelled = true)
    fun onFallDamage(event: EntityDamageEvent) = event.takeIf {
        (it.entity as? Player)?.let { entity -> valid(entity) } == true
                && it.damage > 4.0 && it.cause == EntityDamageEvent.DamageCause.FALL
    }?.apply {
        val victims = entity.getNearbyEntities(5.0, 5.0, 5.0).filterIsInstance(Player::class.java)
            .filter { it.allowedPvP }
        var originDamage = 20.0

        victims.forEach {
            val stomperEvent = PlayerStomperEvent(entity as Player, it)
            Bukkit.getPluginManager().callEvent(stomperEvent)

            if (!stomperEvent.isCancelled) {
                it.damage(if (it.isSneaking) 6.0 else originDamage, entity)
            }
            originDamage -= 8.0
        }
        (entity as Player).playSound(entity.location, Sound.ANVIL_LAND, 15.0f, 15.0f)
        if (damage >= 8.0) damage = 8.0
    }
}