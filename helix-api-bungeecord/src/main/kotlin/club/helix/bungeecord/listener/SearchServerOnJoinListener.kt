package club.helix.bungeecord.listener

import club.helix.components.account.HelixUserLogin
import club.helix.components.server.HelixServer
import club.helix.components.util.HelixAddress
import club.helix.bungeecord.HelixBungee
import club.helix.bungeecord.kotlin.player.account
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority

class SearchServerOnJoinListener(private val plugin: HelixBungee): Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    fun onConnect(event: ServerConnectEvent) = event.apply {
        if (reason != ServerConnectEvent.Reason.JOIN_PROXY) return@apply

        try {
            val premium = player.account.login.type == HelixUserLogin.Type.PREMIUM
            val targetServer = if (premium) HelixServer.MAIN_LOBBY else HelixServer.LOGIN
            val availableServer = targetServer.findAvailable()?.serverName ?: return@apply player.disconnect(TextComponent(
                "${ChatColor.RED}Não há um ${if (premium) "lobby" else "servidor de autenticação"} disponível."))

            plugin.proxy.getServerInfo(availableServer)?.apply {
                println("Conectando ${player.name} em $availableServer...")
                target = this
            } ?: player.disconnect(TextComponent("${ChatColor.RED}Não identificamos o servidor alvo em nossa proxy.\n${ChatColor.RED}Entre em contato com nossa equipe: ${HelixAddress.DISCORD}"))
        }catch (error: Exception) {
            error.printStackTrace()
            isCancelled = true
            player.disconnect(TextComponent("${ChatColor.RED}Não foi possível conectar no servidor.\n§c${ChatColor.RED} Entre em contato com nossa equipe: ${HelixAddress.DISCORD}"))
        }
    }
}