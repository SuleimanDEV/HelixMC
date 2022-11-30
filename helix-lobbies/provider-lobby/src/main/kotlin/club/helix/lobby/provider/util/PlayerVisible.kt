package club.helix.lobby.provider.util

import club.helix.components.account.HelixRank
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.util.VanishPlayers
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerVisible {
    companion object {

        fun handle(player: Player) {
            val user = player.account

            if (!HelixRank.staff(user.tag)) {
                Bukkit.getOnlinePlayers().filter { !it.account.preferences.lobby.playersVisible }.forEach { it.hidePlayer(player) }
            }

            if (user.preferences.lobby.playersVisible) {
                show(player)
            }else hide(player)
        }

        private fun hide(player: Player) {
            Bukkit.getOnlinePlayers().filter { !HelixRank.staff(it.account.tag) }.forEach { player.hidePlayer(it) }
        }

        private fun show(player: Player) = Bukkit.getOnlinePlayers().filter {
            VanishPlayers.canShow(it, player) }.forEach { player.showPlayer(it) }
    }
}