package club.helix.bukkit.util

import org.bukkit.Bukkit

class ServerChat(
    var active: Boolean = true,
    var deactivatedBy: String? = null,
) {
    fun clear() {
        for (i in 0..100) {
            Bukkit.getOnlinePlayers().forEach { it.sendMessage("") }
        }
    }
}