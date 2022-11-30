package club.helix.event.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.event.EventPlugin

class GetConfigCMD(private val plugin: EventPlugin): BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, plugin.game.configurations.keys.toMutableList())
    )

    @CommandOptions(
        name = "getconfig",
        permission = true,
        description = "Ver o valor de uma configuração."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            return sender.sendMessage("§cUtilize /getconfig <config> para o ver valor.")
        }

        val config = args[0].lowercase()
        val value = plugin.game.getConfig(config)
            ?: return sender.sendMessage("§cConfiguração não encontrada.")

        sender.sendMessage("§b\"$config\": $value")
    }
}