package club.helix.components.command.sender

interface Sender<T: Any> {
    val isConsole: Boolean
    val isPlayer: Boolean
    val player: T
}