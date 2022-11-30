package club.helix.duels.gladiator.listener

import club.helix.duels.api.event.GameCreatedEvent
import club.helix.duels.api.game.DuelsPlayer
import club.helix.duels.gladiator.GladiatorGame
import club.helix.duels.gladiator.GladiatorPlugin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class GameArenaGeneratorListener(
    private val plugin: GladiatorPlugin
): Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    fun onGameCreated(event: GameCreatedEvent<GladiatorGame>) = Bukkit.getScheduler().runTaskAsynchronously(plugin) {
        val game = event.game

        val oneMillis = System.currentTimeMillis()
        println("MAP GENERATING... ${event.game.playingPlayers.size}")
        game.mapGenerator.load()
        val twoMillis = System.currentTimeMillis()
        println("MAP GENERATED: ${twoMillis - oneMillis}ms")

        println("TELEPORTED PLAYERS!")
        game.playingPlayers.map(DuelsPlayer::player).forEach { it.teleport(game.mapGenerator.location) }
    }
}