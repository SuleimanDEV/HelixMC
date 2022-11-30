package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandlerCooldown
import club.helix.pvp.arena.kit.provider.event.PlayerNinjaEvent
import club.helix.pvp.arena.kotlin.player.allowedPvP
import club.helix.pvp.arena.kotlin.player.hasSelectedKit
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerToggleSneakEvent

class Ninja: KitHandlerCooldown("ninja-kit", 8) {
    companion object {
        private val data = mutableMapOf<Player, Player>()
    }

    @EventHandler fun onShiftTeleport(event: PlayerToggleSneakEvent) = event.takeIf {
        valid(it.player) && !it.player.isSneaking && data.containsKey(it.player)
    }?.apply {
        if (hasCooldownOrCreate(player)) return@apply sendCooldownMessage(player)

        val target = data[player]!!.takeIf {
            player.location.distance(it.location) <= 50
        } ?: return@apply player.sendMessage("§cEste jogador está a mais de 50 blocos de distância.")

        val ninjaEvent = PlayerNinjaEvent(player, target)
        Bukkit.getPluginManager().callEvent(ninjaEvent)
        if (ninjaEvent.isCancelled) return@apply

        player.teleport(target)
    }

    @EventHandler fun  onEntityDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        (it.damager as? Player)?.let { damager -> damager.allowedPvP && damager.hasSelectedKit(this) } == true
                && (it.entity as? Player)?.allowedPvP == true
    }?.apply { data[damager as Player] = entity as Player }

    @EventHandler fun onPlayerDeath(event: PlayerDeathEvent): Unit = event.run {
        data.remove(entity)
        data.values.removeIf { it.player == entity }
    }
}