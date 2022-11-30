package club.helix.components.account

import kotlinx.serialization.Serializable

@Serializable
class HelixUserPreference(
    var automaticPull: Boolean = true,
    val chat: Chat = Chat(),
    val privacy: Privacy = Privacy(),
    val lobby: Lobby = Lobby(),
    val staff: Staff = Staff(),
    val skin: Skin = Skin()
) {
    @Serializable
    class Skin(
        var skinId: String? = null,
        var lastUpdate: Long = System.currentTimeMillis()
    )

    @Serializable
    class Chat(
        var receiveChatMessage: Boolean = true,
        var receiveClanMessage: Boolean = true,
        var messageFilter: Boolean = true

    )

    @Serializable
    class Privacy(
        var receivePrivateMessage: Boolean = true,
        var receiveFriendshipRequest: Boolean = true,
        var receiveDuelInvitations: Boolean = true,
        var receiveClanInvitations: Boolean = true
    )

    @Serializable
    class Lobby(
        var gameProtection: Boolean = true,
        var playersVisible: Boolean = true,
        var sendJoinMessage: Boolean = true,
    )

    @Serializable
    class Staff(
        var receivedStaffMessage: Boolean = true,
        var notifyReports: Boolean = true
    )
}