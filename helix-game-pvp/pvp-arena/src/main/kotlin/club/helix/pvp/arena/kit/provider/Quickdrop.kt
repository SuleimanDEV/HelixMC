package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.event.SoupRegenerationEvent
import club.helix.pvp.arena.kit.KitHandler
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.inventory.ItemStack

class Quickdrop: KitHandler() {

    @EventHandler fun onRegenerationSoup(event: SoupRegenerationEvent) = event.takeIf {
        valid(it.player)
    }?.apply { item = ItemStack(Material.AIR) }
}