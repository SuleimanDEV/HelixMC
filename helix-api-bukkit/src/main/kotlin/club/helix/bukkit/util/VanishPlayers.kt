package club.helix.bukkit.util

import club.helix.bukkit.kotlin.player.account
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class VanishPlayers {
    companion object {

        fun handleVanish(player: Player) = if (player.account.vanish) {
            Bukkit.getOnlinePlayers().filter {
                it != player && it.account.mainRankLife.rank.isLessThen(player.account.mainRankLife.rank)
            }.forEach { it.hidePlayer(player) }
        }else {
            Bukkit.getOnlinePlayers().forEach { it.showPlayer(player) }
        }

        fun canShow(staff: Player, victim: Player) = !staff.account.vanish ||
                staff.account.mainRankLife.rank.isLessThen(victim.account.mainRankLife.rank)
    }
}