package club.helix.bukkit.precommand.sender

import org.bukkit.entity.Player

interface Sender {
    val isConsole: Boolean
    val isPlayer: Boolean
    val player: Player
}