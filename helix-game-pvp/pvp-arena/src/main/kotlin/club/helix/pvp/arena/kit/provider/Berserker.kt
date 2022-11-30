package club.helix.pvp.arena.kit.provider

import club.helix.bukkit.event.PlayerDeathEvent
import club.helix.pvp.arena.kit.KitHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Berserker: KitHandler() {

    @EventHandler(priority = EventPriority.LOW)
    fun onUserDeath(event: PlayerDeathEvent) = event.killer?.takeIf { valid(it) }?.apply {
        addPotionEffect(PotionEffect(PotionEffectType.SPEED, 6 * 20, 1))
        addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 6 * 20, 0))
        sendMessage("§a[Berserker] Você ganhou efeitos ao matar ${event.player.name}.")
    }
}