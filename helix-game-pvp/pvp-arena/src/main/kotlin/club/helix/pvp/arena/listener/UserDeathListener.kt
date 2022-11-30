package club.helix.pvp.arena.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.event.PlayerDeathEvent
import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.handle.UserDeathHandle
import club.helix.pvp.arena.kotlin.player.arenaPlayer
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class UserDeathListener(private val plugin: PvPArena): Listener {

    private fun runClearDropsTask(items: MutableList<Item>) = plugin.server.scheduler
        .runTaskLater(plugin, { items.forEach(Item::remove) }, 7 * 20)

    @EventHandler fun onDeath(event: PlayerDeathEvent) = event.apply {
        drops.removeIf { ItemBuilder.has(it, "kit") ||
                ItemBuilder.has(it, "find-compass-player") || ItemBuilder.has(it, "cancel-drop")}
        onDroppedItems { runClearDropsTask(it) }

        droppedExp = 0
        newExp = 0
        newLevel = 0

        if (killer == null) {
            player.apply {
                arenaPlayer.pvp = false
                activePotionEffects.forEach { removePotionEffect(it.type) }

                plugin.server.scheduler.runTaskLater(plugin, {
                    plugin.serverSpawnHandle.send(player)
                }, 5)
            }
        }else {
            UserDeathHandle(player, killer, plugin).execute()
        }
    }
}