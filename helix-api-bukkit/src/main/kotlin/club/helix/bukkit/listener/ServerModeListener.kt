package club.helix.bukkit.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent

class ServerModeListener: Listener {

    @EventHandler fun onPlayerLogin(event: PlayerLoginEvent) {
        if (event.result == PlayerLoginEvent.Result.KICK_WHITELIST) {
            event.disallow(
                PlayerLoginEvent.Result.KICK_OTHER,
                "§cServidor acessível somente para membros da equipe."
            )
        }else if (event.result == PlayerLoginEvent.Result.KICK_FULL) {
            event.player.takeIf { !it.hasPermission("helix.group.vip") }?.let {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cServidor cheio. Adquira vip para poder entrar.")
            } ?: event.allow()
        }
    }
}