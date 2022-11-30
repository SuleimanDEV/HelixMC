package club.helix.bukkit.listener

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.clan.handleTagSuffix
import club.helix.bukkit.kotlin.player.account
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserLoadClanListener(private val apiBukkit: HelixBukkit): Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) = Bukkit.getScheduler().runTaskAsynchronously(HelixBukkit.instance) {
        val clanManager = apiBukkit.components.clanManager
        val clan = clanManager.controller.loadByMember(event.player.name)
            ?: return@runTaskAsynchronously println("[CLAN] ${event.player.name} não tem clan!")

        if (clan.leader.name.lowercase() == event.player.name.lowercase()
            && clan.recalculate(event.player.account.mainRankLife.rank)) {
            clanManager.controller.save(clan)
            println("[CLAN] Atualizando clan ${clan.name} leader: ${clan.leader.name}")
        }

        clan.handleTagSuffix(event.player)
        clanManager.register(clan)
        println("[CLAN] clan de ${event.player.name} carregado!")
    }
}