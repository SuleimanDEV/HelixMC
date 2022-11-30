package club.helix.hologram.consumer

import club.helix.hologram.Hologram
import org.bukkit.entity.Player

class HologramUpdatePacketConsumer(
    val hologram: Hologram,
    val player: Player
)