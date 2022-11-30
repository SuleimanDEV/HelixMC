package club.helix.bungeecord.precommand

import club.helix.bungeecord.HelixBungee
import club.helix.bungeecord.precommand.executor.BungeeCommandExecutor
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import club.helix.components.command.CommandMap
import club.helix.components.command.completer.CommandCompleterHandle
import club.helix.components.command.executor.CommandExecutor
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor
import java.util.*

class BungeeCommandMap(protected  val plugin: HelixBungee): CommandMap() {

    override fun createCommand(executor: CommandExecutor<*>) {
        val executeMethod = executor.javaClass.getMethod(
            "execute",
            BungeeCommandSender::class.java,
            Array<String>::class.java
        )

        val options = executeMethod.getAnnotation(CommandOptions::class.java)
            ?: throw NullPointerException("este executor não possui configuração")

        if (commands.containsKey(options.name))
            throw Exception("o executor do comando ${options.name} já foi registrado")

        commands[options.name.lowercase()] = AbstractMap.SimpleEntry(executor, options)
        val bungeeCommand = BungeeCommand(
            options.name.lowercase(),
            options.aliases,
            executor,
            this)
        plugin.proxy.pluginManager.registerCommand(plugin, bungeeCommand)
    }

    fun handle(command: BungeeCommand, sender: CommandSender, args: Array<String>) {
        try {
            val entry = commands[command.name.lowercase()] ?: commands[command.aliases.first { commands.containsKey(it) }]!!
            val bungeeCommandSender = BungeeCommandSender(sender, plugin)

            if (entry.value.permission && !bungeeCommandSender.hasPermission("helix.cmd.${entry.value.name.lowercase()}")) {
                val noPermissionMessage = entry.value.permissionMessage.takeIf { it.isNotEmpty() } ?: "${ChatColor.RED}§cVocê não tem permissão para executar este comando."
                return bungeeCommandSender.message(ComponentBuilder(noPermissionMessage).color(ChatColor.RED).create())
            }

            entry.value.target.takeIf { it != CommandTarget.ALL }?.apply {
                if (this == CommandTarget.PLAYER && !bungeeCommandSender.isPlayer) {
                    return bungeeCommandSender.message(ComponentBuilder("Comando disponível apenas para jogadores.")
                        .color(ChatColor.RED).create())
                }


                if (this == CommandTarget.CONSOLE && !bungeeCommandSender.isConsole) {
                    return bungeeCommandSender.message(
                        ComponentBuilder("Comando disponível apenas pelo console.")
                            .color(ChatColor.RED).create()
                    )
                }
            }
            (entry.key as BungeeCommandExecutor).execute(bungeeCommandSender, args)
        }catch (error: Exception) {
            error.printStackTrace()
        }
    }

    class BungeeCommand(
        name: String,
        aliases: Array<String>,
        private val executor: CommandExecutor<*>,
        private val commandMap: BungeeCommandMap
    ): Command(name, null, *aliases), TabExecutor {
        override fun execute(sender: CommandSender, args: Array<String>) =
            commandMap.handle(this, sender, args)

        override fun onTabComplete(sender: CommandSender, args: Array<String>): MutableIterable<String> = mutableListOf<String>().apply {
            val completes = (executor as BungeeCommandExecutor).onTabComplete(
                BungeeCommandSender(sender, commandMap.plugin)
            )

            completes.filter { it.index == args.size.dec() }.forEach {
                addAll(CommandCompleterHandle(it, args[it.index]).execute())
            }
        }
    }
}