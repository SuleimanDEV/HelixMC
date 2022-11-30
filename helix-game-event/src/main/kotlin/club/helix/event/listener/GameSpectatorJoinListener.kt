package club.helix.event.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.Build.Companion.build
import club.helix.event.EventPlugin
import club.helix.event.player.GamePlayerType
import club.helix.event.player.event.GamePlayerJoinEvent
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class GameSpectatorJoinListener(private val plugin: EventPlugin): Listener {

    @EventHandler fun onGamePlayerJoin(event: GamePlayerJoinEvent) = event.takeIf {
        it.gamePlayer.type == GamePlayerType.SPECTATOR
    }?.apply {
        val player = gamePlayer.player.apply {
            build(false)
            gameMode = GameMode.ADVENTURE
            allowFlight = true
            isFlying = allowFlight
            maxHealth = 20.0
            health = maxHealth
            inventory.clear()
            inventory.armorContents = null
            canPickupItems = false
            activePotionEffects.forEach { removePotionEffect(it.type) }
            addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, Int.MAX_VALUE, 0, true, false))
            teleport(plugin.spawnLocation)
            game.scoreboard.build(this)
        }

        plugin.server.scheduler.runTaskLater(plugin, {
            player.inventory.apply {
                addItem(ItemBuilder()
                    .type(Material.COMPASS)
                    .displayName("§aJogadores")
                    .identify("cancel-drop")
                    .identify("spectator-item", "players")
                    .toItem
                )

                addItem(ItemBuilder()
                    .type(Material.BED)
                    .displayName("§cVoltar ao lobby")
                    .identify("cancel-drop")
                    .identify("spectator-item", "back-to-lobby")
                    .toItem
                )
            }
        }, 2)

        game.players.forEach {
            it.player.hidePlayer(player)

            if (it.type == GamePlayerType.SPECTATOR) {
                player.hidePlayer(it.player)
            }
        }
    }
}