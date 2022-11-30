package club.helix.hologram

import club.helix.hologram.consumer.HologramUpdatePacketConsumer
import club.helix.hologram.line.HologramLine
import org.bukkit.Location
import java.util.function.Consumer

class Hologram(
    private val location: Location,
    vararg lines: HologramLine
) {
    companion object {
        val holograms = mutableListOf<Hologram>()
    }
    val lines = mutableMapOf<Int, HologramLine>()
    val metadata = HologramMetaData()

    val consumers = mutableListOf<Consumer<*>>()

    init {
        lines.forEach { addLine(it) }
        holograms.add(this)
    }

    fun addLine(line: HologramLine) {
        lines[lines.size] = line
        if (isSpawned()) spawn()
    }

    inline fun <reified T: HologramLine> lines() = lines.mapNotNull { it.key as? T }

    fun hasLine(line: Int) = lines.size >= line.inc()

    fun getLine(int: Int) = lines.takeIf { it.size >= int.inc() }?.run { lines[int] }

    @JvmName("getCastLine")
    inline fun <reified T: HologramLine> getLine(line: Int) = lines[line]?.let { it as? T }

    fun spawn() {
        lines.values.forEach(HologramLine::destroy)
        var index = lines.size.dec()

        for (line in lines.values) {
            line.spawn(location.clone().add(0.0, 0.30 * index--, 0.0))
        }
    }


    fun spawn(consumer: Consumer<HologramUpdatePacketConsumer>) {
        spawn()
        consumers.add(consumer)
    }

    inline fun <reified T> addConsumer(consumer: Consumer<T>) {
        this.consumers.add(consumer)
    }

    inline fun <reified T> consumers() = consumers.filterIsInstance<Consumer<T>>()

    fun destroy() = lines.values.forEach(HologramLine::destroy)

    fun isSpawned() = lines.values.any(HologramLine::isSpawn)
}