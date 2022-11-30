package club.helix.lobby.provider.collectible.particle.provider

import club.helix.lobby.provider.collectible.particle.ParticleCollectible
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.wrappers.EnumWrappers
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.cos
import kotlin.math.sin

class AspiralParticle: ParticleCollectible(
    "aspiral-particle",
    "Aspiral",
    1,
    ItemStack(Material.REDSTONE),
    1640217525682
) {

    override fun callRunnable(player: Player) {
        val location = player.location
        val time = playersTime[player.name]
            ?: throw NullPointerException("invalid player time")

        val x = radius * sin(time)
        val z = radius * cos(time)

        val packet = protocolManager.createPacket(PacketType.Play.Server.WORLD_PARTICLES)
            .apply { modifier.writeDefaults() }

        packet.particles.write(0, EnumWrappers.Particle.HEART)

        packet.float.write(0, (location.x + x).toFloat())
            .write(1, (location.y + 2).toFloat())
            .write(2, (location.z + z).toFloat())


        sendServerPacket(packet)
        playersTime[player.name] = time + 0.1
    }
}