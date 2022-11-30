package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandler
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Poseidon: KitHandler() {

    private fun canApplyEffect(player: Player) = valid(player) &&
            player.location.block.type.toString().contains("WATER")

    @EventHandler fun onPlayerMove(event: PlayerMoveEvent) = event.takeIf {
        canApplyEffect(it.player)
    }?.player?.apply {
        addPotionEffect(PotionEffect(PotionEffectType.SPEED, 4 * 20, 1))
        addPotionEffect(PotionEffect(PotionEffectType.WATER_BREATHING, 4 * 20, 0))
        addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20, 0))
    }
}