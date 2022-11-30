package club.helix.lobby.login.listener

import club.helix.lobby.login.LoginLobby
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.*

class CancelPreLoginEventListener(private val plugin: LoginLobby): Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.player.takeIf {
        !plugin.isAuthenticate(it.name)
    }?.run { event.isCancelled = true }

    @EventHandler fun onBlockPlace(event: BlockPlaceEvent) = event.player.takeIf {
        !plugin.isAuthenticate(it.name)
    }?.run { event.isCancelled = true }

    @EventHandler fun onBlockBreak(event: BlockBreakEvent) = event.player.takeIf {
        !plugin.isAuthenticate(it.name)
    }?.run { event.isCancelled = true }

    @EventHandler fun onBucketPlace(event: PlayerBucketEmptyEvent) = event.player.takeIf {
        !plugin.isAuthenticate(it.name)
    }?.run { event.isCancelled = true }

    @EventHandler fun onPortalUse(event: PlayerPortalEvent) = event.player.takeIf {
        !plugin.isAuthenticate(it.name)
    }?.run { event.isCancelled = true }

    @EventHandler fun onDropItem(event: PlayerDropItemEvent) = event.player.takeIf {
        !plugin.isAuthenticate(it.name)
    }?.run { event.isCancelled = true }

    @EventHandler fun onBedEnter(event: PlayerBedEnterEvent) = event.player.takeIf {
        !plugin.isAuthenticate(it.name)
    }?.run { event.isCancelled = true }

    @EventHandler fun onFishEvent(event: PlayerFishEvent) = event.player.takeIf {
        !plugin.isAuthenticate(it.name)
    }?.run { event.isCancelled = true }

    @EventHandler fun onPickupItem(event: PlayerPickupItemEvent) = event.player.takeIf {
        !plugin.isAuthenticate(it.name)
    }?.run { event.isCancelled = true }
}