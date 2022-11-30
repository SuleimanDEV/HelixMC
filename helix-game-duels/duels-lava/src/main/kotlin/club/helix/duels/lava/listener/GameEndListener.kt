package club.helix.duels.lava.listener

import club.helix.bukkit.kotlin.player.teleport
import club.helix.duels.api.event.GameEndEvent
import club.helix.duels.api.game.DuelsPlayer
import club.helix.duels.lava.LavaPlugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameEndListener(
    private val plugin: LavaPlugin
): Listener {

    @EventHandler
    fun onGameEnd(event: GameEndEvent<*>) = event.apply {
        game.playingPlayers.map(DuelsPlayer::player).filter(Player::isOnline).forEach {
            it.teleport(plugin.spawnLocation.clone().apply { y += 2 })
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin) {
            winnerAccount.apply {
                duels.lava.apply {
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
                duels.lava.apply {
                    winstreak = 0
                    defeats++
                    matches++
                }
                plugin.apiBukkit.components.userManager.saveAll(this)
            }
        }
    }
}