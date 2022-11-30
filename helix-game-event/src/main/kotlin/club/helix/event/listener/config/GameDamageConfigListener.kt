package club.helix.event.listener.config

import club.helix.event.EventPlugin
import club.helix.event.player.GamePlayerType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class GameDamageConfigListener(private val plugin: EventPlugin): Listener {

    @EventHandler (ignoreCancelled = true)
    fun onDamage(event: EntityDamageEvent) = event.takeIf {
        plugin.game.getConfig<Boolean>("damage") == false ||
                (it.entity as? Player)?.let { player -> plugin.game.hasPlayer(player, GamePlayerType.PLAYING) } == false
    }?.apply { isCancelled = true }

    @EventHandler (priority = EventPriority.LOWEST)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        (it.damager as? Player)?.let { damager ->
            plugin.game.hasPlayer(damager, GamePlayerType.SPECTATOR)
        } == true
    }?.apply { isCancelled = true }
}