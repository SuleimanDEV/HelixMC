package club.helix.pvp.arena.listener

import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.kotlin.player.unregisterArenaPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class UserQuitListener(private val plugin: PvPArena): Listener {

    @EventHandler (priority = EventPriority.MONITOR)
    fun onUserQuit(event: PlayerQuitEvent) = event.player.apply {
        event.quitMessage = null
        plugin.arenaUserKits.unregister(name)
        plugin.combatLog.remove(name)
        unregisterArenaPlayer()
    }
}