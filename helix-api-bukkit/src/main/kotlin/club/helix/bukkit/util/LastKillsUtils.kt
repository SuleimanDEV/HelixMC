package club.helix.bukkit.util

import org.bukkit.entity.Player

class LastKillsUtils {
    companion object {
        private val lastKills = mutableMapOf<String, MutableList<String>>()

        fun get(player: Player) = lastKills[player.name] ?: mutableListOf<String>().apply {
            lastKills[player.name] = this }

        fun set(player: Player, lastKills: MutableList<String>) = this.lastKills.put(player.name, lastKills)

        fun isRepeatedKill(player: Player, target: Player) = lastKills[player.name]?.filter {
            it == target.name }?.let { it.size > 2 } == true

        fun reset(player: Player) = lastKills.remove(player.name)
    }
}