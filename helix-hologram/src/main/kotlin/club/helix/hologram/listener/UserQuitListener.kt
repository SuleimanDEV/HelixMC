package club.helix.hologram.listener

import club.helix.hologram.Hologram
import club.helix.hologram.HologramListener
import club.helix.hologram.line.HologramPacketLine
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class UserQuitListener(private val hologramListener: HologramListener): Listener {

    @EventHandler fun onQuit(event: PlayerQuitEvent) = Hologram.holograms.removeIf { hologram ->
        hologram.lines.values.filterIsInstance<HologramPacketLine>().any {
            it.player.name == event.player.name
        }
    }
}