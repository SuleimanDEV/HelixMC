package club.helix.pvp.fps.listener

import club.helix.bukkit.event.PlayerDeathEvent
import club.helix.pvp.fps.PvPFps
import club.helix.pvp.fps.handle.UserDeathHandle
import club.helix.pvp.fps.kotlin.player.removePvP
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class UserDeathListener(private val plugin: PvPFps): Listener {

    @EventHandler fun onDeath(event: PlayerDeathEvent) = event.apply {
        drops.clear(); droppedExp = 0; newExp = 0; newLevel = 0
        deathMessage = null

        if (killer == null) {
            player.apply {
                removePvP()
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