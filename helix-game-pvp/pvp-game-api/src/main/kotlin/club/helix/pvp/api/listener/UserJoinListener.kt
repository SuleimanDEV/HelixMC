package club.helix.pvp.api.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.nms.NameTagNMS
import club.helix.pvp.api.GameAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserJoinListener(private val gameAPI: GameAPI): Listener {

    @EventHandler (priority = EventPriority.MONITOR)
    fun onJoin(event: PlayerJoinEvent) = event.apply {
        if (gameAPI.apiBukkit.components.clanManager.hasMemberClan(player.name)) return@apply
        NameTagNMS.setSuffix(player, " ${player.account.pvp.displayRank()}")
    }
}