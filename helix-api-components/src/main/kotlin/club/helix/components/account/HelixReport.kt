package club.helix.components.account

import kotlinx.serialization.Serializable
import java.util.concurrent.TimeUnit

@Serializable
class HelixReport(
    val accused: String,
    private val victims: MutableMap<String, MutableSet<String>> = mutableMapOf(),
    val createdAt: Long = System.currentTimeMillis()
) {
    var remaingTime: Long = 0
        private set

    val priority get() = when {
        size() >= 15 -> Priority.EXTREME
        size() >= 10 -> Priority.HIGHEST
        size() >= 6 -> Priority.HIGH
        size() >= 3 -> Priority.MEDIUM
        else -> Priority.LOW
    }

    fun size() = victims.flatMap { it.value }.size
    fun isNew() = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - createdAt) <= 3
    fun hasExpired() = remaingTime < System.currentTimeMillis()

    fun insert(victim: String, reason: String) {
        if (victims[victim]?.contains(reason) == true) return

        val differenceTime = remaingTime - System.currentTimeMillis()
        var newRemaingTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20)

        if (differenceTime > 0) {
            newRemaingTime += differenceTime
        }

        remaingTime = newRemaingTime
        val reasons = victims[victim] ?: mutableSetOf()
        victims[victim] = reasons.apply { add(reason) }
    }

    fun getVictims() = victims.toMap()

    enum class Priority(val displayName: String) {
        LOW("§aBaixa"),
        MEDIUM("§eMédia"),
        HIGH("§6Alta"),
        HIGHEST("§cMuito alta"),
        EXTREME("§4Extrema");
    }
}