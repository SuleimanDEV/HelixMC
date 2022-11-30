package club.helix.pvp.fps.listener

import club.helix.pvp.fps.PvPFps
import club.helix.pvp.fps.kotlin.player.removePvP
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class UserLeaveListener(private val plugin: PvPFps): Listener {

    @EventHandler fun onQuit(event: PlayerQuitEvent) = event.player.apply {
        removePvP()
        plugin.combatLog.remove(name)
    }
}