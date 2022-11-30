package club.helix.bukkit.npc

import club.helix.hologram.Hologram
import club.helix.hologram.line.HologramSimpleLine
import org.bukkit.Location

open class HelixSimpleNPC(
    header: Array<String>,
    skinOrigin: String,
    skinValue: String,
    skinSignature: String,
    location: Location,
): HelixNPC(
    location = location,
    skinOrigin = skinOrigin,
    skinValue = skinValue,
    skinSignature = skinSignature
) {

    override var hologram: Hologram? = Hologram(location.clone().add(0.0, -0.1, 0.0))

    init {
        header.forEach { hologram!!.addLine(HologramSimpleLine(it)) }
    }
}