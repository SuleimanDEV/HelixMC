package club.helix.duels.uhc.listener

import club.helix.duels.api.event.GameDeleteEvent
import club.helix.duels.uhc.UHCGame
import club.helix.duels.uhc.UHCPlugin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class DeleteArenaListener(private val plugin: UHCPlugin): Listener {

    @EventHandler fun onGamaDelete(event: GameDeleteEvent<UHCGame>) = Bukkit.getScheduler().runTaskAsynchronously(plugin) {
        event.game.mapGenerator.unload()
    }
}