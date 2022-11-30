package club.helix.bungeecord.listener

import club.helix.bungeecord.HelixBungee
import club.helix.bungeecord.precommand.executor.BungeeCommandExecutor
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import club.helix.components.command.completer.CommandCompleterHandle
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.TabCompleteEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class CommandTabCompleteListener(private val plugin: HelixBungee): Listener {

    @EventHandler fun onTabComplete(event: TabCompleteEvent) = event.apply {
        val player = sender as? ProxiedPlayer
            ?: throw ClassCastException("invalid proxied player")
        val args = cursor.split(" ").toMutableList()

        val commandName = args.removeFirst().replace("/", "")
        val index = if (args.isNotEmpty()) args.size.dec() else return@apply
        val input = args[index]

        val command = plugin.commandMap.commands.values.firstOrNull {
            it.value.name.lowercase() == commandName.lowercase()
        } ?: return@apply

        val completes = (command.key as BungeeCommandExecutor)
            .onTabComplete(BungeeCommandSender(player, plugin))

        completes.filter { it.index == index.plus(1) }.forEach { completer ->
            suggestions.addAll(CommandCompleterHandle(completer, input).execute())
        }
    }
}