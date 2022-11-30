package club.helix.bukkit.listener

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.account
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class OnlineUserTimeListener(private val apiBukkit: HelixBukkit): Listener {
    companion object {
        private val data = mutableMapOf<String, Long>()
    }

    @EventHandler fun onJoin(event: PlayerJoinEvent) =
        data.put(event.player.name, System.currentTimeMillis())

    @EventHandler (priority = EventPriority.LOWEST)
    fun onQuit(event: PlayerQuitEvent) = event.player.apply {
        val joinedTime = data[name] ?: throw NullPointerException("invalid joined time")
        val now = System.currentTimeMillis()
        val differenteTime = now - joinedTime

        player.account.apply {
            onlineTime += differenteTime
            println("$name +${differenteTime / 20}s")
            apiBukkit.components.userManager.saveAll(this)
        }
        data.remove(name)
    }
}