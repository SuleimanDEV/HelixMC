package club.helix.bukkit.hologram

import org.bukkit.Location
import java.util.function.Consumer

class HelixHologram(
    private val location: Location,
    vararg texts: String
) {
    companion object {
        val holograms = mutableListOf<HelixHologram>()
    }

    val lines = mutableMapOf<Int, HologramLine>()
    var consumer: Consumer<HelixHologram>? = null

    init {
        holograms.add(this)
        texts.forEach { addLine(it) }
    }

    fun addLine(text: String) {
        lines[lines.size] = HologramLine(text)
    }

    fun respawn() {
        destroy()
        spawn()
    }

    fun spawn() {
        var index = lines.size.dec()

        for (line in lines.values) {
            line.spawn(location.clone().add(0.0, 0.30 * index--, 0.0))
        }
    }

    fun onUpdate(consumer: Consumer<HelixHologram>) {
        this.consumer = consumer
    }

    fun spawn(consumer: Consumer<HelixHologram>) {
        onUpdate(consumer)
        spawn()
    }

    fun destroy(): Unit = lines.values.forEach(HologramLine::destroy)

    fun getLine(index: Int): String? = lines[index]?.text

    fun updateLine(index: Int, text: String) {
        if (index >= lines.size) {
            throw IndexOutOfBoundsException("$index > ${lines.size}")
        }
        lines[index]?.text(text)
    }

    fun isSpawned() = lines.values.any(HologramLine::isSpawned)
}