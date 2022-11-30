package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.server.HelixServer

class FindCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, HelixServer.networkPlayers)
    )

    @CommandOptions(
        name = "find",
        permission = true,
        description = "Localizar jogadores."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            return sender.sendMessage("§cUtilize /find <jogador> para localizar um jogador.")
        }

        val target = args[0]
        val targetServer = HelixServer.getPlayerServer(target)
            ?: return sender.sendMessage("§cJogador offline.")

        sender.sendMessage("§7$target foi localizado: §f${targetServer.displayName} (${targetServer.serverName})")
    }
}