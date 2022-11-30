package club.helix.lobby.provider.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.kotlin.player.sendPlayerTitle
import club.helix.components.account.HelixRank
import club.helix.lobby.provider.util.PlayerVisible
import club.helix.lobby.provider.util.SpawnItems
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserJoinListener: Listener {

    @EventHandler fun onJoin(event: PlayerJoinEvent) = event.player.apply {
        sendPlayerTitle("§d§lHELIX", "§eSeja bem-vindo!", 1)
        event.joinMessage = null

        try {
            val user = account
            val vip = HelixRank.vip(user.mainRankLife.rank)
            PlayerVisible.handle(event.player)

            if (HelixRank.vip(user.tag) && user.preferences.lobby.sendJoinMessage && !user.vanish) {
                event.joinMessage = "${user.tag.prefix}${name} §6entrou neste lobby!"
            }

            gameMode = GameMode.ADVENTURE
            inventory.heldItemSlot = 0
            inventory.clear()
            inventory.armorContents = null
            allowFlight = vip
            isFlying = vip
            activePotionEffects.forEach { removePotionEffect(it.type) }
            health = maxHealth
            fireTicks = 0
            foodLevel = 20
            level = 0
            exp = 0f

            inventory.let { it.clear(); it.armorContents = null; SpawnItems.set(this) }
        } catch (error: Exception) {
            error.printStackTrace()
            event.player.kickPlayer("§cNão foi possível conectar-se a este lobby.")
        }
    }
}