package club.helix.pvp.arena.kit

import club.helix.components.util.HelixTimeData
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit

open class KitHandlerCooldown(
    private val cooldownId: String,
    private val cooldownTime: Long,
    private val cooldownTimeUnit: TimeUnit = TimeUnit.SECONDS
) : KitHandler() {

    open val cooldownMessage = "§cAguarde {time} para utilizar o kit novamente."

    fun sendCooldownMessage(player: Player) = player.sendMessage(
        cooldownMessage.replace("{time}", HelixTimeData.getTimeFormatted(player.name, cooldownId)
            ?: "{error-time}"))

    fun applyCooldown(player: Player) {
        HelixTimeData.putTime(player.name, cooldownId, cooldownTime, cooldownTimeUnit)
    }

    fun hasCooldownOrCreate(player: Player) = HelixTimeData.getOrCreate(player.name, cooldownId,
        cooldownTime, cooldownTimeUnit)

    fun hasCooldown(player: Player) = HelixTimeData.isActive(player.name, cooldownId)
}