package club.helix.components.account

import kotlinx.serialization.Serializable

@Serializable
class HelixUserLogin(
    val firstLogin: Long = System.currentTimeMillis(),
    var lastLogin: Long = System.currentTimeMillis(),
    var type: Type = Type.CRACKED,
    var password: String? = null
) {
    fun isRegistered() = password != null

    enum class Type {
        PREMIUM, CRACKED;
    }
}