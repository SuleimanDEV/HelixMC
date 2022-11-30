package club.helix.components.command

import club.helix.components.command.executor.CommandExecutor
import club.helix.components.command.executor.CommandOptions

abstract class CommandMap {

    val commands = mutableMapOf<String, Map.Entry<CommandExecutor<*>, CommandOptions>>()
    abstract fun createCommand(executor: CommandExecutor<*>)
}