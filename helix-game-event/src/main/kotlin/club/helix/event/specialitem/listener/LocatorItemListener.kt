package club.helix.event.specialitem.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.event.EventPlugin
import club.helix.event.player.GamePlayer
import club.helix.event.player.GamePlayerType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class LocatorItemListener(private val plugin: EventPlugin): Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(it.item, "special-item", "locator")
    }?.apply {
        isCancelled = true

        val nearbyPlayers = plugin.game.getPlayers(GamePlayerType.PLAYING).map(GamePlayer::player).filter { it != player }
            .takeIf { it.isNotEmpty() } ?: return@apply player.sendMessage("§cNão há jogadores para localizar.")

        val target = nearbyPlayers.minByOrNull { player.location.distance(it.location) }!!
        player.compassTarget = target.location
        player.sendMessage("§aBússola apontando para: ${target.name}")
    }
}