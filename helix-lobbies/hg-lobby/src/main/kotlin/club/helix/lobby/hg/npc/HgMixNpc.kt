package club.helix.lobby.hg.npc

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.npc.HelixNPC
import club.helix.components.server.HelixServer
import club.helix.hologram.Hologram
import club.helix.hologram.consumer.HologramUpdateConsumer
import club.helix.hologram.line.HologramSimpleLine
import club.helix.lobby.hg.inventory.HgMixInventory
import org.bukkit.Location

class HgMixNpc: HelixNPC(
    location = Location(HelixBukkit.instance.world, 0.47915559646128425, 66.0, 25.461649099462925, 179.99995f, -1.6500103f),
    skinOrigin = "marvel",
    skinSignature = "",
    skinValue = ""
) {
    override var hologram: Hologram? = Hologram(location.clone().add(0.0, -0.1, 0.0),
        HologramSimpleLine("§bHG Mix"), HologramSimpleLine("§c0 jogando agora"))

    fun load() {
        spawn { HgMixInventory.open(it.clicker) }

        hologram!!.addConsumer<HologramUpdateConsumer> {
            println("call hg mix consumer<HologramUpdateConsumer>!")
            it.hologram.getLine(1)?.text(
                "§e${HelixServer.HG.onlinePlayers(HelixServer.Category.HG_MIX).size} jogando agora"
            )
        }
    }
}