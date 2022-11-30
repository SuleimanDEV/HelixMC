package club.helix.bukkit.command

import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.server.HelixServer

class ConnectCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, HelixServer.values().flatMap {
            it.providers.toList() }.map { it.second.serverName }.toMutableList())
    )

    @CommandOptions(
        name = "connect",
        description = "Conectar-se em um determinado servidor.",
        target = CommandTarget.PLAYER,
        permission = true
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            return sender.sendMessage(arrayOf(
                "§cUtilize /connect <server-name> para se conectar."
            ))
        }
        val target = HelixServer.getServer(args[0])
            ?: return sender.sendMessage("§cServidor não encontrado.")

        sender.player.connect(target)
        sender.sendMessage("§7Efetuando conexão com §f${target.type.displayName} (${target.serverName})§7...")
    }
}