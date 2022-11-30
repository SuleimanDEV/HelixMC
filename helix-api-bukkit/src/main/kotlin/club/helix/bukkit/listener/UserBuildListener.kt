package club.helix.bukkit.listener

import club.helix.bukkit.kotlin.player.Build.Companion.build
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent

class UserBuildListener: Listener {

    @EventHandler (ignoreCancelled = true)
    fun onBuild(event: BlockPlaceEvent) = event.takeIf { !it.player.build }?.apply {
        isCancelled = true }

    @EventHandler (ignoreCancelled = true)
    fun onBreak(event: BlockBreakEvent) = event.takeIf { !it.player.build }?.apply {
        isCancelled = true }

    @EventHandler (ignoreCancelled = true)
    fun onBucketPlace(event: PlayerBucketEmptyEvent) = event.takeIf { !it.player.build }?.apply {
        isCancelled = true }
}