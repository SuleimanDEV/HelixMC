package club.helix.duels.gladiator.listener

import club.helix.bukkit.kotlin.player.Build.Companion.build
import club.helix.duels.api.event.GameEndEvent
import club.helix.duels.api.game.DuelsPlayer
import club.helix.duels.gladiator.GladiatorGame
import club.helix.duels.gladiator.GladiatorPlugin
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameEndListener(
    private val plugin: GladiatorPlugin
): Listener {

    @EventHandler fun onGameEnd(event: GameEndEvent<GladiatorGame>) = event.apply {
        game.playingPlayers.map(DuelsPlayer::player).filter(Player::isOnline).forEach {
            it.build(false)
            it.gameMode = GameMode.ADVENTURE
            it.allowFlight = true
            it.isFlying = it.allowFlight
            it.teleport(game.mapGenerator.location.clone().apply { y += 2 })
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin) {
            winnerAccount.apply {
                duels.gladiator.apply {
                    wins++
                    winstreak++
                    matches++
                    if (maxWinstreak < winstreak) {
                        maxWinstreak = winstreak
                    }
                }
                plugin.apiBukkit.components.userManager.saveAll(this)
            }
            loserAccount.apply {
                duels.gladiator.apply {
                    winstreak = 0
                    defeats++
                    matches++
                }
                plugin.apiBukkit.components.userManager.saveAll(this)
            }
        }
    }
}