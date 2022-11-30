package club.helix.event.listener

import club.helix.event.EventPlugin
import club.helix.event.event.GameStateChangeEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameStateChangeListener(private val plugin: EventPlugin): Listener {

    @EventHandler fun onChangeState(event: GameStateChangeEvent) {
        plugin.updateServerState()
        Bukkit.getOnlinePlayers().forEach(event.game.scoreboard::build)
    }
}