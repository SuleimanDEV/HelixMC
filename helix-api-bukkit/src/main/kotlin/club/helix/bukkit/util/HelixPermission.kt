package club.helix.bukkit.util

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.account
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionAttachment

class HelixPermission(private val plugin: HelixBukkit) {
    companion object {
        private val attachments = mutableMapOf<Player, PermissionAttachment>()
    }

    fun setup(player: Player) {
        val attachment = attachments[player] ?: player.addAttachment(plugin).apply { attachments[player] = this }
        val user = player.account

        if (!user.hasPermission("*")) {
            val permissions = mutableListOf<String>().apply {
                user.ranksLife.forEach { addAll(it.rank.permissions) }
            }

            permissions.forEach { attachment.setPermission(it, true) }
        }else player.isOp = true
    }

    fun recalculate(player: Player) { reset(player); setup(player) }

    fun reset(player: Player) {
        player.isOp = false
        attachments.remove(player)?.remove()
    }
}