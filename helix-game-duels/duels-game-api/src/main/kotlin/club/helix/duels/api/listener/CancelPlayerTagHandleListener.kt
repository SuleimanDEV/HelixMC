package club.helix.duels.api.listener

import club.helix.bukkit.event.PlayerTagHandleEvent
import club.helix.bukkit.nms.NameTagNMS
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class CancelPlayerTagHandleListener: Listener {

    @EventHandler fun onTagHandle(event: PlayerTagHandleEvent) = event.takeIf {
        it.reason == NameTagNMS.Reason.TAG }?.apply { isCancelled = true }
}