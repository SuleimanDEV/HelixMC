package club.helix.pvp.arena.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.weather.WeatherChangeEvent

class ServerEssentialsListener: Listener {

    @EventHandler fun onFoodLevelChange(event: FoodLevelChangeEvent) =
        run { event.isCancelled = true }

    @EventHandler fun onWeather(event: WeatherChangeEvent) =
        event.apply { isCancelled = toWeatherState() }
}