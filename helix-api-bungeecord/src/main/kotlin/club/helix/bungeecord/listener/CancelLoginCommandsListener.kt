package club.helix.bungeecord.listener

import club.helix.components.server.HelixServer
import club.helix.bungeecord.HelixBungee
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class CancelLoginCommandsListener(private val plugin: HelixBungee): Listener {

    @EventHandler fun onChat(event: ChatEvent) = event.takeIf {
        it.message.startsWith("/") && HelixServer.getPlayerServer((it.sender as ProxiedPlayer).name)?.let { server ->
            server.type == HelixServer.LOGIN } == true
    }?.apply {
        val commands = plugin.proxy.pluginManager.commands
            .map { it.value.name }
        val command = message.split(" ")[0].replace("/", "")

        if (commands.any { it.lowercase() == command.lowercase() }) {
            event.isCancelled = true
        }
    }
}