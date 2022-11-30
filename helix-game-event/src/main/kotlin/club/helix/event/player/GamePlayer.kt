package club.helix.event.player

import org.bukkit.entity.Player

class GamePlayer(
    val player: Player,
    var type: GamePlayerType
)