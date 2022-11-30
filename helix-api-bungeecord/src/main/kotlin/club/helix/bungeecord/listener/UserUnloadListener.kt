package club.helix.bungeecord.listener

import club.helix.bungeecord.HelixBungee
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import java.util.concurrent.TimeUnit

class UserUnloadListener(private val plugin: HelixBungee): Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onServerDisconnect(event: PlayerDisconnectEvent) = event.apply {
        try {
            val userManager = plugin.components.userManager

            plugin.proxy.scheduler.runAsync(plugin) {
                val user = userManager.redisController.load(player.name)?.apply {
                    login.lastLogin = System.currentTimeMillis()
                    userManager.removeUser(this)

                    val permissions = mutableListOf<String>().apply {
                        ranksLife.forEach { addAll(it.rank.permissions) }
                    }
                    permissions.forEach { player.setPermission(it, false) }
                } ?: throw NullPointerException("invalid user")

                userManager.saveAll(user, false)
                plugin.proxy.scheduler.schedule(plugin, {
                    userManager.redisController.delete(player.name)
                }, 2, TimeUnit.SECONDS)
            }
        }catch (error: Exception) {
            error.printStackTrace()
        }
    }
}