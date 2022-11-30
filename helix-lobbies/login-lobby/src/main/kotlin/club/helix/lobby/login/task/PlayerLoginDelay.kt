package club.helix.lobby.login.task

import club.helix.lobby.login.LoginLobby
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.TimeUnit

class PlayerLoginDelay(
    private val plugin: LoginLobby
) : BukkitRunnable() {

    override fun run() = Bukkit.getOnlinePlayers().filter {
        plugin.loggingPlayers.containsKey(it.name) && (System.currentTimeMillis() - plugin.loggingPlayers[it.name]!!) >= TimeUnit.SECONDS.toMillis(35)
    }.forEach {
        it.kickPlayer("§cVocê excedeu o tempo de autenticação.")
    }
}