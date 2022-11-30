package club.helix.bukkit.kotlin.player

import club.helix.bukkit.HelixBukkit
import club.helix.components.server.HelixServerProvider
import com.google.common.io.ByteStreams
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

fun Player.connect(server: HelixServerProvider, sendMessage: Boolean = false) {
    try {
        val output = ByteStreams.newDataOutput().apply {
            writeUTF("Connect")
            writeUTF(server.serverName)
        }
        sendPluginMessage(HelixBukkit.instance, "BungeeCord", output.toByteArray())

        if (sendMessage) {
            spigot().sendMessage(TextComponent("§aConectando...").apply {
                hoverEvent = HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    ComponentBuilder("§7Dê: §f${HelixBukkit.instance.currentServer.serverName}\n" +
                            "§7Para: §f${server.serverName}").create())
            })
        }
    }catch (error: Exception) {
        error.printStackTrace()
        sendMessage("§cOcorreu um erro ao conectar-se em ${server.serverName}...")
    }
}