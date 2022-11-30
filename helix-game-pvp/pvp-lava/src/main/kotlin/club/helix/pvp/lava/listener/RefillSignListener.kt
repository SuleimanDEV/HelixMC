package club.helix.pvp.lava.listener

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class RefillSignListener: Listener {

    @EventHandler fun onSignChange(event: SignChangeEvent) = event.takeIf {
        it.getLine(0)?.let { text -> text.startsWith("recraft") || text.startsWith("sopas") } == true
    }?.apply {
        val type = getLine(0)
        setLine(0, "§c-§6-§e-§a-§b-")
        setLine(1, "§b§lHELIX")
        setLine(2, type.uppercase())
        setLine(3, "§c-§6-§e-§a-§b-")
    }

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.clickedBlock?.let { block -> block.type == Material.SIGN ||
                block.type == Material.SIGN_POST || block.type == Material.WALL_SIGN } == true
    }?.apply {
        val sign = (clickedBlock.state as? Sign)?.takeIf {
            it.getLine(0)?.startsWith("§c-§6-§e-§a-§b-") == true
        } ?: return@apply

        val recraft = sign.getLine(2)?.equals("RECRAFT") == true
        val inventory = Bukkit.createInventory(null, 4 * 9, "Recraft")

        if (recraft) {
            for (i in 0 until 36 step 3) {
                inventory.setItem(i, ItemStack(Material.RED_MUSHROOM, 64))
            }
            for (i in 1 until 36 step 3) {
                inventory.setItem(i, ItemStack(Material.BROWN_MUSHROOM, 64))
            }
            for (i in 2 until 36 step 3) {
                inventory.setItem(i, ItemStack(Material.BOWL, 64))
            }
        }else {
            for (i in 0 until inventory.size) {
                inventory.addItem(ItemStack(Material.MUSHROOM_SOUP))
            }
        }

        player.openInventory(inventory)
    }
}