package club.helix.components.server.provider

import club.helix.components.server.HelixServer
import club.helix.components.server.HelixServerProvider

class LobbyProvider(
    displayName: String,
    serverName: String
): HelixServerProvider(HelixServer.Category.LOBBY, displayName, serverName) {
    override val available get(): Boolean = online
}