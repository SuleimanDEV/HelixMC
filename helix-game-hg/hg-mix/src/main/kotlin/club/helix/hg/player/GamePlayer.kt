package club.helix.hg.player

import org.bukkit.entity.Player

class GamePlayer(
    val player: Player,
    val type: GamePlayerType,
    var kills: Int = 0
)