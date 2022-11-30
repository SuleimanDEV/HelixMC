package club.helix.bukkit.precommand.sender

import club.helix.components.command.sender.CommandSender
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.permissions.PermissionAttachmentInfo
import org.bukkit.plugin.Plugin

class BukkitCommandSender(
    private val sender: org.bukkit.command.CommandSender
): CommandSender<Player>(), org.bukkit.command.CommandSender {

    override val isConsole get(): Boolean = (sender !is Player)

    override val isPlayer get(): Boolean = (sender is Player)

    override val player get() = (sender as Player)

    override fun isOp() = sender.isOp

    override fun setOp(value: Boolean) {
        sender.isOp = value
    }

    override fun isPermissionSet(name: String) = sender.isPermissionSet(name)

    override fun isPermissionSet(perm: Permission) = sender.isPermissionSet(perm)

    override fun hasPermission(permission: String) = sender.hasPermission(permission)

    override fun hasPermission(perm: Permission) = sender.hasPermission(perm)

    override fun addAttachment(plugin: Plugin, name: String, value: Boolean): PermissionAttachment = sender.addAttachment(plugin, name, value)

    override fun addAttachment(plugin: Plugin): PermissionAttachment = sender.addAttachment(plugin)

    override fun addAttachment(plugin: Plugin, name: String, value: Boolean, ticks: Int): PermissionAttachment? = sender.addAttachment(plugin, name, value, ticks)

    override fun addAttachment(plugin: Plugin, ticks: Int): PermissionAttachment? = sender.addAttachment(plugin, ticks)

    override fun removeAttachment(attachment: PermissionAttachment) {
        sender.removeAttachment(attachment)
    }

    override fun recalculatePermissions() {
        sender.recalculatePermissions()
    }

    override fun getEffectivePermissions(): MutableSet<PermissionAttachmentInfo> = sender.effectivePermissions

    override fun sendMessage(message: String) {
        sender.sendMessage(message)
    }

    override fun sendMessage(messages: Array<out String>) {
        sender.sendMessage(messages)
    }

    override fun getServer(): Server = sender.server

    override fun getName(): String = sender.name

}