package club.helix.lobby.pvp.listener

import club.helix.lobby.pvp.PvPLobby
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class SpawnUserDistanceListener(private val plugin: PvPLobby): Listener {

    @EventHandler fun onMove(event: PlayerMoveEvent) = event.takeIf {
        it.player.location.distance(plugin.spawnLocation) >= 150
    }?.apply { player.teleport(plugin.spawnLocation) }
}