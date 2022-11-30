package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Vampire: KitHandler() {

    @EventHandler (priority = EventPriority.LOW)
    fun onPlayerDeath(event: PlayerDeathEvent) = event.entity.killer?.takeIf { valid(it) }?.apply {
        addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 2))
        sendMessage("§a[Vampire] Você ganhou regeneração ao matar ${event.entity.name}.")
    }
}