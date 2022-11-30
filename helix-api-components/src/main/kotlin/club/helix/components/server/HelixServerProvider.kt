package club.helix.components.server

import kotlinx.serialization.Serializable

@Serializable
open class HelixServerProvider(
    val category: HelixServer.Category,
    val displayName: String,
    val serverName: String
) {
    var online: Boolean = false
    lateinit var type: HelixServer
    var id: Int = 0
    var maxPlayers = 0
    var onlinePlayers = mutableListOf<String>()

    open val available get(): Boolean = online && onlinePlayers.size < maxPlayers

    fun findAvailableLobby(): HelixServerProvider? {
        val target = when {
            type.toString().contains("DUELS") && category != HelixServer.Category.LOBBY -> HelixServer.DUELS
            type.toString().contains("PVP") && category != HelixServer.Category.LOBBY -> HelixServer.PVP
            else -> HelixServer.MAIN_LOBBY
        }

        return target.findAvailable(HelixServer.Category.LOBBY)
    }

    fun toPair() = serverName to this
}