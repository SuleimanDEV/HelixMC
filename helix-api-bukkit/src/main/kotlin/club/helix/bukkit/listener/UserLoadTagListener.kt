package club.helix.bukkit.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.nms.NameTagNMS
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class UserLoadTagListener: Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onJoin(event: PlayerJoinEvent) = event.player.apply {
        NameTagNMS.setNametag(this, account.tag, NameTagNMS.Reason.TAG)
    }

    @EventHandler fun onQuit(event: PlayerQuitEvent) = event.player.apply {
        NameTagNMS.data.remove(name) }
}