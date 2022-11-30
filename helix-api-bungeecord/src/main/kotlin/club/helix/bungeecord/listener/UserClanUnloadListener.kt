package club.helix.bungeecord.listener

import club.helix.bungeecord.HelixBungee
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class UserClanUnloadListener(private val plugin: HelixBungee): Listener {

    @EventHandler fun onQuit(event: PlayerDisconnectEvent) = plugin.components.clanManager.apply {
        val clan = getClanByMember(event.player.name) ?: return@apply
        val member = clan.getMember(event.player.name) ?: return@apply

        if (clan.members.none {
                ProxyServer.getInstance().getPlayer(it.name) != null && member != it
        }) {
            unregister(clan)
            println("[CLAN] O clan de ${event.player.name} foi removido do cache.")
        }
        invites.remove(event.player.name)
    }
}