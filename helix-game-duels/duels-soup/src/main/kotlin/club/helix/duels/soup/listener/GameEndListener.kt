package club.helix.duels.soup.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.kotlin.player.teleport
import club.helix.duels.api.event.GameEndEvent
import club.helix.duels.api.game.DuelsPlayer
import club.helix.duels.soup.SoupPlugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class GameEndListener(
    private val plugin: SoupPlugin
): Listener {

    @EventHandler (priority = EventPriority.HIGH)
    fun onGameEnd(event: GameEndEvent<*>) = event.apply {
        game.playingPlayers.map(DuelsPlayer::player).filter(Player::isOnline).forEach {
            it.teleport(plugin.spawnLocation.clone().apply { y += 2 })
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin) {
            winner.player.account.apply {
                duels.soup.apply {
                    wins++
                    winstreak++
                    matches++
                    if (maxWinstreak < winstreak) {
                        maxWinstreak = winstreak
                    }
                }
                plugin.apiBukkit.components.userManager.saveAll(this)
            }
            loser?.player?.account?.apply {
                duels.soup.apply {
                    winstreak =0
                    defeats++
                    matches++
                }
                plugin.apiBukkit.components.userManager.saveAll(this)
            }
        }
    }
}