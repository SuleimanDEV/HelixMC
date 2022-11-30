package club.helix.lobby.login.listener

import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
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

    @EventHandler fun onDamage(event: EntityDamageEvent) =
        run { event.isCancelled = true }

    @EventHandler fun onDamageByEntity(event: EntityDamageByEntityEvent) =
        run { event.isCancelled = true }
}