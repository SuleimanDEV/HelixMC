package club.helix.bukkit.listener

import club.helix.bukkit.HelixBukkit
import club.helix.components.HelixComponents
import club.helix.components.util.HelixTimeData
import com.squareup.moshi.Types
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class UserTimeDataListener(private val apiBukkit: HelixBukkit): Listener {

    private val adapterType = Types.newParameterizedType(MutableMap::class.java, Any::class.java, Long::class.javaObjectType)
    private val adapter = HelixComponents.MOSHI.adapter<MutableMap<Any, Long>>(adapterType)

    @EventHandler (priority = EventPriority.MONITOR)
    fun onJoin(event: PlayerJoinEvent) = apiBukkit.components.redisPool.resource.use {
        val serialized = it.get("times:${event.player.name}") ?: return@use

        try {
            val activeTimes = adapter.fromJson(serialized)
                ?: throw NullPointerException("invalid activef times")
            HelixTimeData.setTimes(event.player.name, activeTimes)
        }catch (error: Exception) {
            error.printStackTrace()
        }
    }

    @EventHandler fun onQuit(event: PlayerQuitEvent) = event.player.apply {
        val activeTimes = HelixTimeData.getActiveTimes(name) ?: return@apply apiBukkit.components.redisPool
            .resource.use { if (it.exists("times:$name")) it.del("times:$name") }

        try {
            HelixTimeData.delete(name)
            apiBukkit.components.redisPool.resource.use {
                it.set("times:$name", adapter.toJson(activeTimes.toMutableMap()))
            }
        }catch (error: Exception) {
            error.printStackTrace()
        }
    }
}