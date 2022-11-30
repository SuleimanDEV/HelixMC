package club.helix.components.server.provider

import club.helix.components.server.HelixServer
import club.helix.components.server.HelixServerProvider
import java.util.concurrent.TimeUnit

class HardcoreGamesProvider(
    category: HelixServer.Category,
    displayName: String,
    serverName: String,
): HelixServerProvider(category, displayName, serverName) {
    var state: State = State.WAITING
    var time: Int = 0

    override val available get(): Boolean = online

    enum class State(val initTime: Int = 0) {
        WAITING(TimeUnit.MINUTES.toSeconds(5).toInt()),
        INVINCIBILITY(TimeUnit.MINUTES.toSeconds(2).toInt()),
        PLAYING(-1),
        ENDED
    }
}