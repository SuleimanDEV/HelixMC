package club.helix.pvp.arena.listener

import club.helix.bukkit.event.PlayerDeathEvent
import club.helix.bukkit.kotlin.player.account
import club.helix.components.account.HelixRank
import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.vip.CoinsMultiplier
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ClanReceiveExpListener(private val plugin: PvPArena): Listener {

    @EventHandler fun onDeath(event: PlayerDeathEvent) = event.killer?.apply {
        val clan = plugin.apiBukkit.components.clanManager.getClanByMember(name) ?: return@apply
        val rank = account.mainRankLife.rank
        val multiplier = CoinsMultiplier(rank)

        val addedExp = multiplier.calcule().apply {
            clan.exp += this
        }
        plugin.apiBukkit.components.clanManager.controller.save(clan)
        sendMessage("§3+$addedExp clan exp${if (HelixRank.vip(rank)) " (Bônus vip: ${multiplier.boost}x)" else ""} ")
    }
}