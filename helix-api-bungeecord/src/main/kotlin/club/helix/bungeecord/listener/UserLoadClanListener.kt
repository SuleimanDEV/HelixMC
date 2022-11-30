package club.helix.bungeecord.listener

import club.helix.bungeecord.HelixBungee
import club.helix.bungeecord.kotlin.player.account
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority

class UserLoadClanListener(private val plugin: HelixBungee): Listener {

    @EventHandler (priority = EventPriority.NORMAL)
    fun onConnect(event: ServerConnectEvent) = event.takeIf {
        it.reason == ServerConnectEvent.Reason.JOIN_PROXY
    }?.apply {
        if (plugin.components.clanManager.hasMemberClan(player.name)) return@apply
        val clan = plugin.components.clanManager.controller.loadByMember(player.name) ?: return@apply

        if (clan.leader.name.lowercase() == event.player.name.lowercase()
            && clan.recalculate(event.player.account.mainRankLife.rank)) {
            plugin.components.clanManager.controller.save(clan)
            println("[CLAN] Atualizando clan ${clan.name} leader: ${clan.leader.name}")
        }
        plugin.components.clanManager.register(clan)
    }
}