package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.server.HelixServer

class OnlineCMD: BukkitCommandExecutor() {

    @CommandOptions(
        name = "online",
        permission = true,
        description = "Visualizar estatísticas dos servidores."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        sender.sendMessage("§6Servidores:")

        HelixServer.values().forEach { mainServer ->
            if (!mainServer.single) {
                sender.sendMessage("\n§2  ${mainServer.displayName}: §f${mainServer.onlinePlayers.size}")

                mainServer.providers.values.forEach {
                    sender.sendMessage("§7    ${it.displayName}: §f${it.onlinePlayers.size}")}
            }else sender.sendMessage("\n§2  ${mainServer.displayName}: §f${mainServer.onlinePlayers.size}")
        }

        sender.sendMessage("\n§6Total online: §f${HelixServer.networkPlayers.size}\n")
    }
}