package club.helix.hologram

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.util.function.Consumer

class PacketInteractListener(private val hologramListener: HologramListener) {

    fun register() = ProtocolLibrary.getProtocolManager().addPacketListener(object: PacketAdapter(
        hologramListener.plugin, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY
    ) {
        override fun onPacketReceiving(event: PacketEvent): Unit = event.run {
            val entityId = packet.integers.read(0)
            val hologram = Hologram.holograms.firstOrNull {
                it.lines.values.any { line -> line.hasEntity(entityId) }
            } ?: return

            val interactEvent = PlayerInteractEvent(
                player, Action.PHYSICAL, null, null, null
            )

            hologram.consumers.filterIsInstance<Consumer<PlayerInteractEvent>>()
                .forEach { it.accept(interactEvent) }
        }
    })
}