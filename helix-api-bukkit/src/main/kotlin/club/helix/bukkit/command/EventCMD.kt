package club.helix.bukkit.command

import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.server.HelixServer

class EventCMD: BukkitCommandExecutor() {

    @CommandOptions(
        name = "evento",
        target = CommandTarget.PLAYER,
        description = "Ir para o evento."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val eventServer = HelixServer.SPECIAL.findAvailable(HelixServer.Category.EVENT)
            ?: return sender.sendMessage("§cNão há um servidor de evento disponível.")
        sender.player.connect(eventServer, true)
    }
}