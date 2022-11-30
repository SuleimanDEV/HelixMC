package club.helix.event.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.event.EventPlugin

class SetConfigCMD(private val plugin: EventPlugin): BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, plugin.game.configurations.keys.toMutableList())
    )

    @CommandOptions(
        name = "setconfig",
        permission = true,
        description = "Alterar configurações do evento."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.size < 2) {
            return sender.sendMessage("§cUtilize /setconfig <config> <value>.")
        }

        val configName = args[0].lowercase()
        val configValue = plugin.game.getConfig(configName)
            ?: return sender.sendMessage("§cConfiguração não encontrada.")

        val newValueBuilder = StringBuilder()
        for (i in 1 until args.size) {
            newValueBuilder.append(args[i]).append(" ")
        }

        var newValue: Any = newValueBuilder.toString().trim()

        try {
            newValue = plugin.game.convertConfigInstance(configValue, newValue)
        }catch (ignored: NullPointerException) {
            return sender.sendMessage("§cEsta configuração necessita de um valor do tipo ${configValue::class.java.simpleName}.")
        }

        if (configValue == newValue) {
            return sender.sendMessage("§c\"$configName\" já possui este valor.")
        }

        plugin.game.setConfig(configName, newValue)
        plugin.notifyPlayers.forEach {
            it.sendMessage("§a${sender.name} definiu \"${configName}\" para §f$newValue§a.")
        }
    }
}