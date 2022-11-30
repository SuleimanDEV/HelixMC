package club.helix.hologram

import club.helix.hologram.consumer.HologramUpdateConsumer
import club.helix.hologram.consumer.HologramUpdatePacketConsumer
import club.helix.hologram.line.HologramPacketLine
import org.bukkit.scheduler.BukkitRunnable

class HologramUpdateTask: BukkitRunnable() {

    override fun run() = Hologram.holograms.forEach { hologram ->
        hologram.consumers<Hologram>().forEach {
            it.accept(hologram)
        }

        hologram.lines<HologramPacketLine>().forEach { packetLine ->
            val hologramUpdatePacketConsumer = HologramUpdatePacketConsumer(
                hologram, packetLine.player
            )
            hologram.consumers<HologramUpdatePacketConsumer>().forEach {
                it.accept(hologramUpdatePacketConsumer)
            }
        }
    }
}