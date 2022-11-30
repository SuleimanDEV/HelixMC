package club.helix.lobby.provider.collectible.listener

import club.helix.lobby.provider.collectible.CollectibleManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class CollectibleUserListener(private val collectibleManager: CollectibleManager): Listener {

    @EventHandler (priority = EventPriority.MONITOR)
    fun onJoin(event: PlayerJoinEvent) = collectibleManager.userController.load(event.player)?.let { collectibles ->
        collectibles.removeIf { !collectibleManager.hasCollectiblePermission(event.player, it) }

        collectibles.forEach { it.onEnable(event.player) }
        collectibleManager.set(event.player, collectibles.toMutableList())
    }

    @EventHandler fun onQuit(event: PlayerQuitEvent) = event.player.apply {
        val activeCollectibles = collectibleManager.getSelectedCollectibles(this)
            .takeIf { it.isNotEmpty() } ?: return@apply
        activeCollectibles.forEach { it.onDisable(this) }
    }
}