package club.helix.pvp.arena.listener

import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.kotlin.player.allowedPvP
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class CancelSpawnDamageListener(private val plugin: PvPArena): Listener {

    @EventHandler fun onEntityDamage(event: EntityDamageEvent) = event.takeIf {
        it.entity is Player && !(it.entity as Player).allowedPvP && it.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK
    }?.run { isCancelled = true }

    @EventHandler fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        it.entity is Player && !(it.entity as Player).allowedPvP && it.damager is Player
                && (it.damager as Player).gameMode != GameMode.CREATIVE
    }?.run { isCancelled = true }
}