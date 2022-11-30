package club.helix.pvp.arena.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.pvp.arena.kotlin.player.allowedPvP
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class FindCompassPlayerListener: Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(event.item, "find-compass-player")
    }?.apply {
        isCancelled = true

        val pvpPlayers = Bukkit.getOnlinePlayers().filter {
            it != player && it.allowedPvP && !it.account.vanish
        }.takeIf { it.isNotEmpty() } ?: return@apply player.sendMessage("§cNão há jogadores na arena.")

        val findPlayer = pvpPlayers.minByOrNull { player.location.distance(it.location) }!!
        player.compassTarget = findPlayer.location

        player.sendMessage("§aBússola apontando para ${findPlayer.name}.")
    }
}