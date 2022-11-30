package club.helix.pvp.arena.listener

import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.handle.UserDeathHandle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.TimeUnit

class UserLastKillOnQuitListener(private val plugin: PvPArena): Listener {
    companion object {
        private val lastDamages = mutableMapOf<String, AbstractMap.SimpleEntry<Player, Long>>()
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onHit(event: EntityDamageByEntityEvent) = event.takeIf {
        it.damager is Player
    }?.apply {
        lastDamages[entity.name] = AbstractMap.SimpleEntry(damager as Player, System.currentTimeMillis())
    }

    @EventHandler fun onDeath(event: PlayerDeathEvent) = lastDamages.remove(event.entity.name)

    @EventHandler (priority = EventPriority.LOWEST)
    fun onQuit(event: PlayerQuitEvent) = event.takeIf {
        lastDamages.contains(it.player.name)
    }?.apply {
        val lastDamage = lastDamages[player.name]!!
        val differenceTime = System.currentTimeMillis() - lastDamage.value

        if (TimeUnit.MILLISECONDS.toSeconds(differenceTime) <= 5) {
            lastDamages.remove(player.name)
            UserDeathHandle(player, lastDamage.key, plugin).execute()
        }
    }
}