package club.helix.duels.api.listener

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class SoupSystemListener: Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) && it.player.health < it.player.maxHealth &&
               it.player.itemInHand?.let { item -> item.type == Material.MUSHROOM_SOUP } == true
    }?.player?.apply {
        event.isCancelled = true
        health = if (health + 7 >= 20.0) 20.0 else health + 7
        itemInHand = ItemStack(Material.BOWL)
    }
}