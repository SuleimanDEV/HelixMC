package club.helix.pvp.fps.listener

import club.helix.pvp.fps.PvPFps
import club.helix.pvp.fps.kotlin.player.allowedPvP
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class ClearArenaDropsListener(private val plugin: PvPFps): Listener {

    @EventHandler fun onDrop(event: PlayerDropItemEvent) = event.takeIf {
        it.player.allowedPvP } ?.apply { itemDrop.remove() }
}