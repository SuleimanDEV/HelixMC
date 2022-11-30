package club.helix.pvp.arena.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.kotlin.player.sendPlayerTitle
import club.helix.components.account.HelixRank
import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.kotlin.player.registerArenaPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserJoinListener(private val plugin: PvPArena): Listener {

    @EventHandler fun onUserJoin(event: PlayerJoinEvent) = event.apply {
        joinMessage = null
        player.registerArenaPlayer()

        plugin.apply {
            scoreboard.build(player)
            arenaUserKits.load(player.name)
            serverSpawnHandle.send(player)
        }
        player.sendPlayerTitle("", "§b§lARENA", 1)

        joinMessage = player.account.takeIf { HelixRank.vip(it.tag) && !it.vanish }?.run {
            "${tag.color}${player.name} §7entrou."
        }
    }
}