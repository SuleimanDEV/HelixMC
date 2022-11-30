package club.helix.hologram.line

import net.minecraft.server.v1_8_R3.*
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

class HologramPacketLine(
    val player: Player,
    text: String
) : HologramLine(text) {

    private var entityArmorStand: EntityArmorStand? = null

    override fun text(text: String) {
        this.text = text

        entityArmorStand?.apply {
            customName = text

            val packet = PacketPlayOutEntityMetadata(id, dataWatcher, true)
            (player as? CraftPlayer)?.handle?.playerConnection?.sendPacket(packet)
        }
    }

    override fun spawn(location: Location) {
        val worldServer = (location.world as CraftWorld).handle
        destroy()

        (entityArmorStand ?: EntityArmorStand(
            worldServer, location.x, location.y, location.z
        ).apply { entityArmorStand = this }).apply {
            isInvisible = true
            isInvulnerable(DamageSource.GENERIC)
            customNameVisible = true
            customName = text
        }

        val packet = PacketPlayOutSpawnEntityLiving(entityArmorStand)
        (player as? CraftPlayer)?.handle?.playerConnection?.sendPacket(packet)
    }


    override fun hasEntity(entityId: Int) =
        entityArmorStand?.let {it.id == entityId } == true

    override fun isSpawn(): Boolean = entityArmorStand?.let { !it.dead } == true

    override fun destroy() {
        val entityId = entityArmorStand?.id ?: return
        val packet = PacketPlayOutEntity(entityId)
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

}