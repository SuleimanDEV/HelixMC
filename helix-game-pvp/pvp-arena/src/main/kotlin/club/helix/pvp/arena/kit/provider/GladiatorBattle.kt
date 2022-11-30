package club.helix.pvp.arena.kit.provider

import org.bukkit.Location
import org.bukkit.entity.Player

class GladiatorBattle(
    val p1: GladiatorPlayer,
    val p2: GladiatorPlayer,
) {
    private val players = mutableListOf(p1, p2)
    fun getWinner(loser: GladiatorPlayer) = players.firstOrNull { it != loser }

    fun getPlayer(player: String) = players.firstOrNull {
        it.player.name.lowercase() == player.lowercase()
    }

    fun contains(player: String) = players.any {
        it.player.name.lowercase() == player.lowercase()
    }

    class GladiatorPlayer(
        val player: Player,
        val oldLocation: Location
    )
}