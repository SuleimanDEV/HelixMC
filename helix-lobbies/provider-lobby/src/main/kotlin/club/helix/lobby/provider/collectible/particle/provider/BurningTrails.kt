package club.helix.lobby.provider.collectible.particle.provider

import club.helix.lobby.provider.collectible.particle.ParticleCollectible
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.wrappers.EnumWrappers
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.cos
import kotlin.math.sin

class BurningTrails: ParticleCollectible(
    "burning-trails-particle",
    "Trilha Flamejante",
    20,
    ItemStack(Material.BLAZE_POWDER),
    1640217525682
) {
    override val radius: Double = 3.0

    override fun callRunnable(player: Player) {
        val location = player.location
        val time = (playersTime[player.name] ?: 0.0) + 0.1
        playersTime[player.name] = time

        val x = radius * cos(time)
        val z = radius * sin(time)
        var y = location.y
        val packet = protocolManager.createPacket(PacketType.Play.Server.WORLD_PARTICLES)
            .apply { modifier.writeDefaults() }

        packet.particles.write(0, EnumWrappers.Particle.FLAME)

        packet.float.write(0, (location.x + x).toFloat())
            .write(1, (location.y + time).toFloat())
            .write(2, (location.z + z).toFloat())

        sendServerPacket(packet)
        y += 0.1
    }
}