package club.helix.hologram.line

import org.bukkit.Location
import org.bukkit.entity.ArmorStand

class HologramSimpleLine(text: String): HologramLine(text) {

    private var armorStand: ArmorStand? = null

    override fun text(text: String) {
        this.text = text
        armorStand?.customName = text
    }

    override fun spawn(location: Location) {
        armorStand = location.world.spawn(location, ArmorStand::class.java).apply {
            customName = text
            isCustomNameVisible = true
            canPickupItems = false
            isVisible = false
            setGravity(false)
        }
    }

    override fun isSpawn(): Boolean = armorStand?.let { !it.isDead && it.isVisible } == true
    override fun destroy(): Unit = run { armorStand?.remove() }

    override fun hasEntity(entityId: Int) =
        armorStand?.let { it.entityId == entityId } == true
}