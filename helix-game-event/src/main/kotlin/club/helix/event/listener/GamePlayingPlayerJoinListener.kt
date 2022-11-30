package club.helix.event.listener

import club.helix.bukkit.kotlin.player.Build.Companion.build
import club.helix.bukkit.kotlin.player.account
import club.helix.event.EventPlugin
import club.helix.event.player.GamePlayerType
import club.helix.event.player.event.GamePlayerJoinEvent
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GamePlayingPlayerJoinListener(private val plugin: EventPlugin): Listener {

    @EventHandler fun onGamePlayerJoin(event: GamePlayerJoinEvent) = event.takeIf {
        it.gamePlayer.type == GamePlayerType.PLAYING
    }?.apply {
        val tag = gamePlayer.player.account.tag
        game.scoreboard.build(gamePlayer.player)

        val maxPlayers = game.getConfig<Int>("max-players") ?: 100
        Bukkit.broadcastMessage("${tag.color}${gamePlayer.player.name} §eentrou no evento! §e(§b${game.players.size}§e/§b$maxPlayers§b§e)")

        gamePlayer.player.apply {
            build(game.getConfig<Boolean>("build") == true)
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