package club.helix.hg.listener

import club.helix.bukkit.kotlin.player.Build.Companion.build
import club.helix.hg.HgPlugin
import club.helix.hg.player.GamePlayerType
import club.helix.hg.player.event.GamePlayerJoinEvent
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GamePlayingPlayerJoinListener(private val plugin: HgPlugin): Listener {

    @EventHandler fun onGamePlayerJoin(event: GamePlayerJoinEvent) = event.takeIf {
        it.gamePlayer.type == GamePlayerType.PLAYING
    }?.apply {
        game.scoreboard.build(gamePlayer.player)

        gamePlayer.player.apply {
            build(game.build)
            gameMode = GameMode.SURVIVAL
            inventory.clear()
            inventory.armorContents = null
            maxHealth = 20.0
            health = maxHealth
            foodLevel = 20
            allowFlight = false
            isFlying = false
            activePotionEffects.forEach { removePotionEffect(it.type) }
            teleport(plugin.spawnLocation)
        }
    }
}