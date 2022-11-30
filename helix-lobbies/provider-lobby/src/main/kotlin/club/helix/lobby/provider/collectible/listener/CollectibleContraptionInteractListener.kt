package club.helix.lobby.provider.collectible.listener

import club.helix.lobby.provider.collectible.CollectibleCategory
import club.helix.lobby.provider.collectible.CollectibleManager
import club.helix.lobby.provider.collectible.contraption.ContraptionCollectible
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class CollectibleContraptionInteractListener(private val collectibleManager: CollectibleManager): Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.apply {
        val contraption = collectibleManager.getSelectedCollectible<ContraptionCollectible>(player, CollectibleCategory.CONTRAPTION)?.takeIf {
            hasItem() && item == it.item
        } ?: return@apply

        contraption.interactHandle(player)
    }

}