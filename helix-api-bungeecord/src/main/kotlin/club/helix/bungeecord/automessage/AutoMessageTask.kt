package club.helix.bungeecord.automessage

import club.helix.bungeecord.HelixBungee
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.scheduler.ScheduledTask
import java.util.concurrent.TimeUnit

class AutoMessageTask(private val plugin: HelixBungee) {
    companion object {
        private val lastMessages = mutableListOf<AutoMessage>()
    }

    fun run(): ScheduledTask = plugin.proxy.scheduler.schedule(plugin, {
        if (ProxyServer.getInstance().players.isEmpty()) return@schedule

        if (lastMessages.size == AutoMessage.values().size) {
            lastMessages.clear()
        }
        val autoMessage = AutoMessage.values().first { !lastMessages.contains(it) }.apply {
            lastMessages.add(this) }
        val prefix = "§b§lHELIX §7» "

        plugin.proxy.broadcast(*ComponentBuilder(prefix)
            .append(autoMessage.component).create())
    }, 0, 3, TimeUnit.MINUTES)
}