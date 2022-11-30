package club.helix.bungeecord.precommand.executor

import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandExecutor

abstract class BungeeCommandExecutor: CommandExecutor<BungeeCommandSender>() {

    abstract override fun execute(sender: BungeeCommandSender, args: Array<String>)
    override fun onTabComplete(sender: BungeeCommandSender) = mutableListOf<CommandCompleter>()
}