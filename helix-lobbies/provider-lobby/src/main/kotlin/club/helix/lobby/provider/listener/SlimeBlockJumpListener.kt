package club.helix.lobby.provider.listener

import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class SlimeBlockJumpListener: Listener {

    @EventHandler fun onMove(event: PlayerMoveEvent) = event.takeIf {
        it.player.location.block.getRelative(BlockFace.DOWN).type == Material.SLIME_BLOCK
    }?.player?.apply {
        velocity = location.direction.setY(0.3).multiply(2.0)
    }
}