package club.helix.lobby.login

import club.helix.bukkit.HelixBukkit
import club.helix.lobby.login.command.LoginCMD
import club.helix.lobby.login.command.RegisterCMD
import club.helix.lobby.login.listener.*
import club.helix.lobby.login.scoreboard.LoginScoreboard
import club.helix.lobby.login.scoreboard.RegisterScoreboard
import club.helix.lobby.login.task.PlayerLoginDelay
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class LoginLobby: JavaPlugin() {

    private val authenticatedPlayers = mutableSetOf<String>()
    val loggingPlayers = mutableMapOf<String, Long>()
    val loginScoreboard = LoginScoreboard()
    val registerScoreboard = RegisterScoreboard()

    val apiBukkit = Bukkit.getPluginManager().getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid api (HelixBukkit)")

    override fun onEnable() {
        server.worlds.forEach {
            it.time = 1000
            it.setGameRuleValue("doDaylightCycle", "false")
            it.setGameRuleValue("doMobSpawning", "false")
        }

        apiBukkit.commandMap.apply {
            createCommand(LoginCMD(this@LoginLobby))
            createCommand(RegisterCMD(this@LoginLobby))
        }
        loadListeners()
        PlayerLoginDelay(this).runTaskTimer(this, 0, 3 * 20L)
        server.consoleSender.sendMessage("§e§lLOGIN: §fPlugin habilitado! §e[v${description.version}]")
    }

    fun isAuthenticate(name: String) = authenticatedPlayers.contains(name)
    fun setAuthenticate(name: String) = authenticatedPlayers.add(name)
    fun removeAuthenticate(name: String) = authenticatedPlayers.remove(name)

    private fun loadListeners(): Unit = server.pluginManager.let {
        it.registerEvents(CancelPreLoginEventListener(this), this)
        it.registerEvents(CancelPreLoginCommandListener(this), this)
        it.registerEvents(UserAuthenticateListener(this), this)
        it.registerEvents(UserLogoutListener(this), this)
        it.registerEvents(UserJoinListener(this), this)
        it.registerEvents(ServerEssentialsListener(), this)
        it.registerEvents(CancelLoginEventListener(), this)
        it.registerEvents(CancelPremiumLoginListener(), this)
        it.registerEvents(JoinQuitMessageListener(), this)
    }
}