package club.helix.bungeecord.precommand.sender

import club.helix.bungeecord.HelixBungee
import club.helix.components.command.sender.CommandSender
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.connection.ProxiedPlayer

class BungeeCommandSender(
    private val sender: net.md_5.bungee.api.CommandSender,
    private val plugin: HelixBungee
): CommandSender<ProxiedPlayer>(), net.md_5.bungee.api.CommandSender {

    override val player: ProxiedPlayer get() = sender as ProxiedPlayer

    override val isPlayer: Boolean = sender is ProxiedPlayer

    override val isConsole: Boolean = !isPlayer

    override fun getName(): String = sender.name

    fun message(component: Array<BaseComponent>) =
        sendMessage(*component)

    override fun sendMessage(message: String) = sender.sendMessage(message)

    override fun sendMessage(vararg message: BaseComponent) = sender.sendMessage(*message)

    override fun sendMessage(message: BaseComponent) = sender.sendMessage(message)

    override fun sendMessages(vararg messages: String) = sender.sendMessages(*messages)

    override fun getGroups(): MutableCollection<String> = sender.groups

    override fun addGroups(vararg groups: String) = sender.addGroups(*groups)

    override fun removeGroups(vararg groups: String) = sender.removeGroups(*groups)

    override fun hasPermission(permission: String): Boolean = isConsole ||
            plugin.components.userManager.getUser(player.name)?.hasPermission(permission) == true

    override fun setPermission(permission: String, value: Boolean) = sender.setPermission(permission, value)

    override fun getPermissions(): MutableCollection<String> = sender.permissions
}