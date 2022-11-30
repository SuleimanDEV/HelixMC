package club.helix.bukkit.kotlin.player

import org.bukkit.entity.Player

class Build {
    companion object {
        private val enableBuildPlayers = mutableSetOf<String>()

        val Player.build get() = enableBuildPlayers.contains(name.lowercase())
        fun Player.build(value: Boolean) = if (value) {
            enableBuildPlayers.add(name.lowercase())
        }else {
                enableBuildPlayers.remove(name.lowercase())
        }
    }
}