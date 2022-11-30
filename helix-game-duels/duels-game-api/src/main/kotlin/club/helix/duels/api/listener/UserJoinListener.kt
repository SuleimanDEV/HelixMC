package club.helix.duels.api.listener

import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserJoinListener: Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    fun onJoin(event: PlayerJoinEvent) = event.player.apply {
        event.joinMessage = null

        resetTitle()
        gameMode = GameMode.ADVENTURE
        maxHealth = 20.0
        health = maxHealth
        allowFlight = false
        isFlying = allowFlight
        level = 0; exp = 0f
        inventory.clear()
        inventory.armorContents = null
        activePotionEffects.forEach { removePotionEffect(it.type) }
    }
}