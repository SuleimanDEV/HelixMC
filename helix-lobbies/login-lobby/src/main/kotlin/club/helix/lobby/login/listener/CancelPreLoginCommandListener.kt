package club.helix.lobby.login.listener

import club.helix.lobby.login.LoginLobby
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class CancelPreLoginCommandListener(private val plugin: LoginLobby): Listener {
    companion object {
        private val allowCommands = arrayOf("login", "logar", "register", "registrar")
    }

    @EventHandler fun executeCommand(event: PlayerCommandPreprocessEvent) = event.message.split(" ").first().replace("/", "").apply {
        val authenticated = plugin.isAuthenticate(event.player.name)

        allowCommands.takeIf { !authenticated && !it.contains(this) }?.run {
            event.isCancelled = true
        }
    }
}