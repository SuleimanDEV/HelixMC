package club.helix.bukkit.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.util.VanishPlayers
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class StaffVanishListener: Listener {

    @EventHandler fun onUserJoin(event: PlayerJoinEvent)  {
        Bukkit.getOnlinePlayers().filter { it.account.vanish }.forEach {
            VanishPlayers.handleVanish(it) }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    fun onJoin(event: PlayerJoinEvent) = event.takeIf {
        it.player.account.vanish }?.apply { player.sendMessage("§8Você está no modo vanish.") }
}