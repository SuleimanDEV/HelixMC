package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandler
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Camel: KitHandler() {

    @EventHandler fun onMove(event: PlayerMoveEvent) = event.takeIf {
        valid(it.player) && it.player.location.block.getRelative(BlockFace.DOWN).type == Material.SAND
    }?.apply {
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 5 * 20, 1))
        player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 1))
        player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20, 1))
    }
}