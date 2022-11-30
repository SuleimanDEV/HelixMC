package club.helix.bungeecord.listener

import club.helix.bungeecord.kotlin.player.account
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PermissionCheckEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.command.ConsoleCommandSender
import net.md_5.bungee.event.EventHandler

class PermissionCheckListener: Listener {

    @EventHandler
    fun onCheck(event: PermissionCheckEvent) = event.apply {
        try {
            if (sender is ConsoleCommandSender) return@apply setHasPermission(true)

            val user = (sender as ProxiedPlayer).account
            setHasPermission(user.hasPermission("*") || user.hasPermission(permission))
        }catch (error: NullPointerException) {
            setHasPermission(sender.permissions.contains(permission))
        }
    }
}