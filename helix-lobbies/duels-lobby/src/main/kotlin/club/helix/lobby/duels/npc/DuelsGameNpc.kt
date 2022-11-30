package club.helix.lobby.duels.npc

import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.npc.HelixNPC
import club.helix.components.server.HelixServer
import club.helix.hologram.Hologram
import club.helix.hologram.line.HologramSimpleLine
import org.bukkit.Location

open class DuelsGameNpc(
    private val displayName: String,
    private val category: HelixServer.Category,
    skinOrigin: String,
    skinValue: String,
    skinSignature: String,
    location: Location
): HelixNPC(
    location = location,
    skinOrigin = skinOrigin,
    skinValue = skinValue,
    skinSignature = skinSignature
){
    fun load() {
        hologram = Hologram(location.clone().add(0.0, -0.1, 0.0),
            HologramSimpleLine(displayName), HologramSimpleLine("§cAguardando..."))

        hologram!!.addConsumer<Hologram> {
            val onlineCount = HelixServer.DUELS.onlinePlayers(category).size
            val online = HelixServer.DUELS.providers(category).any { provider -> provider.online }

            it.getLine<HologramSimpleLine>(1)?.text(
                if (online) "§e$onlineCount jogando agora" else "§cAguardando..."
            )
        }

        spawn {
            val availableServer = HelixServer.DUELS.findAvailable(category)
                ?: return@spawn it.clicker.sendMessage("§cNão há uma sala disponível.")

            it.clicker.sendMessage("§aSala encontrada!")
            it.clicker.connect(availableServer, true)
        }
    }
}