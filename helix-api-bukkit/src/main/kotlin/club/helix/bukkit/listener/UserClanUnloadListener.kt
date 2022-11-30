package club.helix.bukkit.listener

import club.helix.bukkit.HelixBukkit
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class UserClanUnloadListener(private val apiBukkit: HelixBukkit): Listener {

    @EventHandler fun onQuit(event: PlayerQuitEvent) = apiBukkit.components.clanManager.apply {
        val clan = getClanByMember(event.player.name) ?: return@apply
        val member = clan.getMember(event.player.name) ?: return@apply

        if (clan.members.filter {
            Bukkit.getPlayer(it.name) != null && member != it
        }.isEmpty()) {
            unregister(clan)
            println("[CLAN] O clan de ${event.player.name} foi removido do cache.")
        }
        invites.remove(event.player.name)
    }
}