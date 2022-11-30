package club.helix.bungeecord.listener

import club.helix.components.server.HelixServer
import club.helix.bungeecord.HelixBungee
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class UserTryConnectListener(private val plugin: HelixBungee): Listener {

    @EventHandler fun onRedirect(event: ServerConnectEvent) {
        if (event.reason == ServerConnectEvent.Reason.SERVER_DOWN_REDIRECT || event.reason == ServerConnectEvent.Reason.LOBBY_FALLBACK) {
            val availableLobby = HelixServer.MAIN_LOBBY.findAvailable()?.serverName
                ?: return event.player.disconnect(TextComponent("${ChatColor.RED}Não há um lobby disponível."))

            plugin.proxy.getServerInfo(availableLobby)?.apply {
                event.player.sendMessage(TextComponent("${ChatColor.GREEN}Você foi enviado para §f$availableLobby§7. (${event.reason})"))
                event.target = this
            } ?: event.player.disconnect(TextComponent("§cO servidor o qual você estava foi desligado\n§ce não encontramos nenhum lobby disponível."))
        }
    }
}