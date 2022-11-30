package club.helix.bukkit.precommand.executor

import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandExecutor

abstract class BukkitCommandExecutor: CommandExecutor<BukkitCommandSender>() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf<CommandCompleter>()

    abstract override fun execute(sender: BukkitCommandSender, args: Array<String>)
}