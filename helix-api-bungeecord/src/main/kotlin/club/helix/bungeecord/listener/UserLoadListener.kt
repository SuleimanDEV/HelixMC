package club.helix.bungeecord.listener

import club.helix.bungeecord.HelixBungee
import club.helix.components.account.HelixUser
import club.helix.components.account.HelixUserLogin
import club.helix.components.fetcher.MojangFetcher
import club.helix.components.util.HelixAddress
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import java.util.logging.Level

class UserLoadListener(private val plugin: HelixBungee): Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    fun onServerConnected(event: ServerConnectEvent) = event.apply {
        if (reason != ServerConnectEvent.Reason.JOIN_PROXY) return@apply
        val userManager = plugin.components.userManager

        try {
            val user = (userManager.userSqlController.load(player.name)
                ?: HelixUser(player.name)).apply { userManager.userSqlController.save(this) }

            val premium = MojangFetcher.isPremium(user.name)
            val remainType = if (premium) HelixUserLogin.Type.PREMIUM else HelixUserLogin.Type.CRACKED
            if (user.login.type != remainType) {
                user.login.type = remainType
            }

            userManager.redisController.save(user)
            userManager.putUser(user)

            val permissions = mutableListOf<String>().apply {
                user.ranksLife.forEach { addAll(it.rank.permissions) }
            }

            permissions.forEach { player.setPermission(it, true) }
            plugin.logger.log(Level.INFO, "Conta carregada: ${player.name}")
        }catch (error: Exception) {
            player.disconnect(TextComponent("§c§lHelix Network\n\n§cSua conta não foi carregada corretamente.\n§cEntre em contato com nossa equipe: §b${HelixAddress.DISCORD}"))
            error.printStackTrace()
        }
    }
}