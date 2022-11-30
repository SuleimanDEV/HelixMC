package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.server.HelixServer
import org.bukkit.Bukkit

class GotoCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, HelixServer.networkPlayers)
    )

    @CommandOptions(
        name = "goto",
        target = CommandTarget.PLAYER,
        permission = true,
        description = "Ir até o servidor de algum jogador."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            return sender.sendMessage("§cUtilize /goto <jogador> para ir até o servidor do mesmo.")
        }

        val target = args[0].takeIf { it.lowercase() != sender.name.lowercase()}
            ?: return sender.sendMessage("§cVocê não pode ir até sí mesmo.")

        val targetServer = HelixServer.getPlayerServer(target)
            ?: return sender.sendMessage("§cJogador offline.")

        if (targetServer.type == HelixServer.LOGIN) {
            return sender.sendMessage("§cEste jogador está no servidor de login.")
        }

        if (HelixBukkit.instance.currentServer.serverName == targetServer.serverName) {
            val targetPlayer = Bukkit.getPlayer(target)
                ?: return sender.sendMessage("§cJogador offline.")

            sender.player.teleport(targetPlayer)
            return sender.sendMessage("§dTeleportado para ${targetPlayer.name}.")
        }

        sender.player.connect(targetServer)
        sender.sendMessage("§7Conectando no servidor de $target... §8(${targetServer.displayName})")
    }
}