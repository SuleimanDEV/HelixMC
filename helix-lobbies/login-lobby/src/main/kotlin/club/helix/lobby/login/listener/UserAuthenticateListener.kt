package club.helix.lobby.login.listener

import club.helix.components.server.HelixServer
import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.kotlin.player.sendPlayerTitle
import club.helix.lobby.login.LoginLobby
import club.helix.lobby.login.event.UserAuthenticateEvent
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class UserAuthenticateListener(private val plugin: LoginLobby): Listener {

    @EventHandler fun onAuthenticate(event: UserAuthenticateEvent) = event.player.apply {
        plugin.setAuthenticate(name)
        playSound(location, Sound.ORB_PICKUP, 10.0f, 10.0f)
        sendPlayerTitle("§a§lAutenticado", "§fRedirecionando...")
        plugin.loggingPlayers.remove(name)

        plugin.server.scheduler.runTaskLater(plugin, {
            HelixServer.MAIN_LOBBY.findAvailable(HelixServer.Category.LOBBY)?.apply {
                connect(this)
            } ?: return@runTaskLater kickPlayer("§cNão há um lobby disponível.")
        }, 1 * 20L)
    }
}