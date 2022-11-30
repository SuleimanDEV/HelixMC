package club.helix.duels.api.listener

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class GamePlayersVisibleListener: Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    fun onJoin(event: PlayerJoinEvent) = Bukkit.getOnlinePlayers().forEach {
        event.player.hidePlayer(it)
        it.hidePlayer(event.player)
    }
}