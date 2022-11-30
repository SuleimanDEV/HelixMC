package club.helix.lobby.provider.npc

import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.npc.HelixNPC
import club.helix.components.server.HelixServer
import club.helix.components.server.HelixServerProvider
import club.helix.hologram.Hologram
import club.helix.hologram.line.HologramSimpleLine
import org.bukkit.Location

open class DisplayNpcServer(
    private val header: Array<String>,
    private val target: HelixServer,
    private val counter: HelixServer.Category? = null,
    private val connect: HelixServer.Category,
    private val skinOrigin: String,
    private val skinValue: String,
    private val skinSignature: String,
    val location: Location,
    private val enableOnlineUpdater: Boolean = true
) {
    private lateinit var hologram: Hologram
    lateinit var npc: HelixNPC

    fun load() {
        hologram = Hologram(location.clone().add(0.0, -0.1, 0.0))
        header.forEach { hologram.addLine(HologramSimpleLine(it)) }

        hologram.takeIf { enableOnlineUpdater }?.run {
            addLine(HologramSimpleLine("§c0 joogando agora"))

            addConsumer<Hologram> {
                val online = target.providers(connect).any(HelixServerProvider::online)
                val line = lines.size - 1

                getLine(line)?.text(if (online)
                    "§e${counter?.run { target.onlinePlayers(this).size } ?: target.onlinePlayers.size} jogando agora"
                else "§cAguardando...")
            }
        }

        HelixNPC(
            location = location,
            skinOrigin = skinOrigin,
            skinValue = skinValue,
            skinSignature = skinSignature,
            hologram = hologram
        ).spawn {
            target.findAvailable(connect)?.apply {
                it.clicker.connect(this, true)
            } ?: return@spawn it.clicker.sendMessage("§cServidor indisponível no momento.")
        }
    }
}