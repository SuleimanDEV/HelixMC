package club.helix.hologram.line

import org.bukkit.Location

abstract class HologramLine(text: String) {

    var text = text
        protected set

    abstract fun text(text: String)
    abstract fun spawn(location: Location)

    abstract fun isSpawn(): Boolean
    abstract fun destroy()
    abstract fun hasEntity(entityId: Int): Boolean
}