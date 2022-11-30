package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandler
import club.helix.pvp.arena.kit.provider.event.PlayerStomperEvent
import org.bukkit.event.EventHandler

class SteelHead: KitHandler() {

    @EventHandler (ignoreCancelled = true)
    fun onStomper(event: PlayerStomperEvent) = event.takeIf {
        valid(it.victim) }?.apply { isCancelled = true }
}