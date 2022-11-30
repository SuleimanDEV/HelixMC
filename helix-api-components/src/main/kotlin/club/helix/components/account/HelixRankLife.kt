package club.helix.components.account

import club.helix.components.util.HelixTimeFormat
import kotlinx.serialization.Serializable

@Serializable
data class HelixRankLife(
    val rank: HelixRank,
    val time: Long = 0
) {
    val permanent = time == 0L

    fun formatTime(): String {
        if (permanent) return "Permanente"
        if (time < System.currentTimeMillis()) return "Expirado"

        val differenteTime = time - System.currentTimeMillis()
        return HelixTimeFormat.format(differenteTime)
    }
}