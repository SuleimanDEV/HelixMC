package club.helix.duels.gladiator.listener

import club.helix.duels.api.event.GameDeleteEvent
import club.helix.duels.gladiator.GladiatorGame
import club.helix.duels.gladiator.GladiatorPlugin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class DeleteArenaListener(private val plugin: GladiatorPlugin): Listener {

    @EventHandler fun onGamaDelete(event: GameDeleteEvent<GladiatorGame>) = Bukkit.getScheduler().runTaskAsynchronously(plugin) {
        event.game.mapGenerator.unload()
    }
}