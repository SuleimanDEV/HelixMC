package club.helix.components.command.executor

import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.sender.CommandSender

abstract class CommandExecutor<T: CommandSender<*>> {

    open fun onTabComplete(sender: T) = mutableListOf<CommandCompleter>()

    abstract fun execute(sender: T, args: Array<String>)
}