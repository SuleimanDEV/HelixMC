package club.helix.bukkit.listener

import club.helix.bukkit.event.PlayerDeathEvent
import club.helix.bukkit.util.LastKillsUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class PlayerDeathListener: Listener {

    @EventHandler (priority = EventPriority.MONITOR)
    fun onPlayerDeath(originEvent: org.bukkit.event.entity.PlayerDeathEvent) {
        val deathLocation = originEvent.entity.location.clone()
        val entity = originEvent.entity
        val killer = originEvent.entity.killer

        killer?.apply {
            val lastKills = LastKillsUtils.get(this).apply {
                if (size >= 3) removeLastOrNull()
            }
            LastKillsUtils.set(this, lastKills.apply { add(entity.name) })
        }

        val newEvent = PlayerDeathEvent(
            entity, killer, mutableListOf(*originEvent.drops.toTypedArray()),
            deathLocation, null, originEvent.droppedExp, originEvent.newLevel, originEvent.newExp,
            originEvent.newTotalExp, originEvent.keepLevel, originEvent.keepInventory, deathLocation
        )
        Bukkit.getPluginManager().callEvent(newEvent)

        originEvent.drops.clear()
        originEvent.deathMessage = newEvent.deathMessage ?: null
        originEvent.droppedExp = newEvent.droppedExp
        originEvent.newLevel = newEvent.newLevel
        originEvent.newExp = newEvent.newExp
        originEvent.newTotalExp = newEvent.newTotalExp
        originEvent.keepInventory = newEvent.isKeepInventory
        originEvent.keepLevel = newEvent.isKeepLevel

        val droppedItems = mutableListOf<Item>()

        newEvent.drops.forEach {
            droppedItems.add(newEvent.dropsLocation.world.dropItem(newEvent.dropsLocation, it))
        }
        newEvent.droppedItems?.accept(droppedItems)
    }
}