package club.helix.bukkit.hologram

import org.bukkit.Location
import org.bukkit.entity.ArmorStand

class HologramLine(
    var text: String,
) {
    
    private var entity: ArmorStand? = null

    fun text(text: String) {
        this.text = text
        entity?.customName = text
    }

    fun spawn(location: Location) {
        entity = location.world.spawn(location, ArmorStand::class.java).apply {
            customName = text
            isCustomNameVisible = true
            canPickupItems = false
            isVisible = false
            setGravity(false)
        }
    }

    fun isSpawned() = entity?.let { !it.isDead } == true

    fun destroy() = entity?.remove()
}