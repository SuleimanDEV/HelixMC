package club.helix.pvp.arena.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.inventory.KitsInventory
import club.helix.pvp.arena.kotlin.player.arenaPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class OpenKitInventoryListener(private val plugin: PvPArena): Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(event.item, "select-kit")
    }?.run {
        isCancelled = true

        try {
            val kitNumber = ItemBuilder.get(item, "select-kit")!!.toIntOrNull()
                ?: throw NullPointerException("kit number is invalid!")

            KitsInventory.open(player, player.arenaPlayer, kitNumber)
        }catch (error: Exception) {
            error.printStackTrace()
            player.sendMessage("§8Ocorreu um erro com a página de kits.")
        }
    }
}