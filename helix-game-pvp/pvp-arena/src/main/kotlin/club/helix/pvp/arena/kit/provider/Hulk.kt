package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandlerCooldown
import club.helix.pvp.arena.kotlin.player.allowedPvP
import club.helix.pvp.arena.kotlin.player.hasSelectedKit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent

class Hulk: KitHandlerCooldown("hulk-kit", 6) {

    @EventHandler fun onInteractPlayer(event: PlayerInteractEntityEvent) = event.takeIf {
        it.player.allowedPvP && it.player.hasSelectedKit(this) && it.rightClicked is Player && (it.rightClicked as Player).allowedPvP
                && it.player.itemInHand.type == Material.AIR && it.player.passenger == null
    }?.apply {
        if (hasCooldownOrCreate(player)) return@apply sendCooldownMessage(player)
        player.passenger = rightClicked
    }

    @EventHandler fun onPushPlayer(event: EntityDamageByEntityEvent) = event.takeIf {
        (it.damager as? Player)?.let { damager -> valid(damager) } == true &&
                it.damager.passenger?.let { passenger -> it.entity == passenger } == true
    }?.damager?.apply {
        val passenger = passenger.apply { leaveVehicle() }
        passenger.velocity = (this as Player).location.direction.setY(1.6f).multiply(0.9)
    }
}