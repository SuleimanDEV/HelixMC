package club.helix.lobby.provider.listener

import org.bukkit.GameMode
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockIgniteEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.weather.WeatherChangeEvent

class ServerEssentialsListener: Listener {

    @EventHandler fun onWeather(event: WeatherChangeEvent) =
        run { event.isCancelled = true }

    @EventHandler fun onFood(event: FoodLevelChangeEvent) =
        run { event.isCancelled = true }

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.player.takeIf {
        it.gameMode != GameMode.CREATIVE }?.run { event.isCancelled = true }

    @EventHandler fun onDamage(event: EntityDamageEvent) = event.takeIf {
        it.entity is Player }?.run { isCancelled = true }

    @EventHandler fun onDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        it.entity is Player }?.run { isCancelled = true }

    @EventHandler fun onBlockIgnite(event: BlockIgniteEvent) = event.takeIf {
        it.cause == BlockIgniteEvent.IgniteCause.EXPLOSION ||
                it.cause == BlockIgniteEvent.IgniteCause.LAVA
    }?.apply { isCancelled = true }

    @EventHandler fun onBlockExplode(event: BlockExplodeEvent) =
        event.apply { isCancelled = true }

    @EventHandler fun onEntityDeath(event: EntityDeathEvent) = event.takeIf {
        it.entityType != EntityType.PLAYER
    }?.apply { drops.clear() }
}