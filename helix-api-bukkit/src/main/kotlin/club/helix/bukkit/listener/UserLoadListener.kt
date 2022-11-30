package club.helix.bukkit.listener

import club.helix.bukkit.HelixBukkit
import club.helix.components.util.HelixAddress
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerPreLoginEvent

class UserLoadListener(private val apiBukkit: HelixBukkit): Listener {

    @EventHandler
    fun onLogin(event: PlayerLoginEvent) = event.player.apply {
        apiBukkit.permissionManager.setup(this)
    }

    @EventHandler (priority = EventPriority.LOWEST)
    fun onLogin(event: AsyncPlayerPreLoginEvent) = event.apply {
        if (event.loginResult != AsyncPlayerPreLoginEvent.Result.ALLOWED) return@apply

        try {
            val userManager = apiBukkit.components.userManager
            val user = userManager.redisController.load(name)
                ?: throw NullPointerException("invalid user account")

            userManager.putUser(user)
            Bukkit.getServer().consoleSender.sendMessage("§e§lAPI: §fConta carregada: §e$name §f(Redis)")
        }catch (error: Exception) {
            disallow(PlayerPreLoginEvent.Result.KICK_OTHER,
                "§cSua conta não foi carregada corretamente.\n§cEntre em contato com nossa equipe: §b${HelixAddress.DISCORD}")
            error.printStackTrace()
        }
    }
}