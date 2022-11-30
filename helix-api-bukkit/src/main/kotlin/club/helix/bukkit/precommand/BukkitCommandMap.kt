package club.helix.bukkit.precommand

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.CommandMap
import club.helix.components.command.completer.CommandCompleterHandle
import club.helix.components.command.executor.CommandExecutor
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import org.bukkit.Bukkit
import org.bukkit.command.Command
import java.util.*

class
BukkitCommandMap: CommandMap() {

    fun unregisterCommand(vararg commands: String) {
        val commandMapField = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        commandMapField.isAccessible = true

        val commandMap = commandMapField.get(Bukkit.getServer())
        val knownCommandsField = commandMap.javaClass.getDeclaredField("knownCommands")
        knownCommandsField.isAccessible = true

        val knowCommands = knownCommandsField.get(commandMap) as MutableMap<*, *>
        commands.forEach { knowCommands.remove(it) }
    }

    fun remapCommands() {
        val commandMapField = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        commandMapField.isAccessible = true

        val commandMap = commandMapField.get(Bukkit.getServer())
        val knownCommandsField = commandMap.javaClass.getDeclaredField("knownCommands")
        knownCommandsField.isAccessible = true

        val knowCommands = knownCommandsField.get(commandMap) as MutableMap<*, *>
        knowCommands.keys.removeIf { it.toString().contains(":") }
    }

    override fun createCommand(executor: CommandExecutor<*>) {
        val executeMethod = executor.javaClass.getMethod(
            "execute",
            BukkitCommandSender::class.java,
            Array<String>::class.java
        )

        val commandOptions = executeMethod.getAnnotation(CommandOptions::class.java)
            ?: throw NullPointerException("invalid command options annotation")
        registerCommand(executor, commandOptions)
    }

    private fun registerCommand(executor: CommandExecutor<*>, options: CommandOptions) {
        val entry = AbstractMap.SimpleEntry(executor, options)
        commands[options.name.lowercase().trim()] = entry

        options.aliases.forEach {
            commands[it.lowercase().trim()] = entry
        }

        val bukkitCommand = BukkitCommand(
            options.name,
            options.description,
            options.aliases,
            executor, this
        )

        options.takeIf { it.permission }?.apply {
            bukkitCommand.let {
                it.permission = "helix.cmd.${name.lowercase()}"
                it.permissionMessage = permissionMessage
            }
        }

        val commandMapField = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        commandMapField.isAccessible = true

        val commandMap = commandMapField.get(Bukkit.getServer())
        commandMap.javaClass.getMethod("register", String::class.java, Command::class.java)
            .invoke(commandMap, HelixBukkit.instance.name.lowercase(), bukkitCommand)
    }

    private fun handle(command: BukkitCommand, sender: org.bukkit.command.CommandSender, args: Array<String>) {
        if (!commands.containsKey(command.label.lowercase())) return

        val entry = commands[command.label.lowercase()]!!
        val commandSender = BukkitCommandSender(sender)

        command.permission?.takeIf { entry.value.permission && !commandSender.hasPermission(it) }?.run {
            return commandSender.sendMessage(entry.value.permissionMessage.takeIf { it.isNotEmpty() }
                ?: "§cVocê não tem permissão para executar este comando."
            )
        }

        entry.value.target.takeIf { it != CommandTarget.ALL }?.apply {
            if (this == CommandTarget.PLAYER && !commandSender.isPlayer)
                return commandSender.sendMessage("§cComando disponível apenas para jogadores.")

            if (this == CommandTarget.CONSOLE && !commandSender.isConsole)
                return commandSender.sendMessage("§cComando disponível apenas pelo console.")
        }

        (entry.key as BukkitCommandExecutor).execute(commandSender, args)
    }

    class BukkitCommand(
        name: String,
        description: String,
        aliases: Array<out String>,
        val executor: CommandExecutor<*>,
        val commandLoader: BukkitCommandMap
    ): Command(name, description, "", aliases.toMutableList()) {
        override fun execute(sender: org.bukkit.command.CommandSender, label: String, args: Array<String>): Boolean {
            commandLoader.handle(this, sender, args)
            return true
        }

        override fun tabComplete(
            sender: org.bukkit.command.CommandSender,
            alias: String,
            args: Array<String>
        ): MutableList<String> = mutableListOf<String>().apply {
            val bukkitCommandSender = BukkitCommandSender(sender)
            val completes = (executor as BukkitCommandExecutor).onTabComplete(bukkitCommandSender)

            completes.filter {
                it.index == args.size.dec()
            }.forEach {
                addAll(CommandCompleterHandle(it, args[it.index]).execute())
            }
        }
    }
}