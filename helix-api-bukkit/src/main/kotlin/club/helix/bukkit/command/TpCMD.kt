package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class TpCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, Bukkit.getOnlinePlayers().map(Player::getName).toMutableList())
    )

    @CommandOptions(
        name = "tp",
        description = "Teleportar-se para jogadores.",
        permission = true,
        target = CommandTarget.PLAYER
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            return sender.sendMessage("§cUtilize /tp <jogador> para teleportar-se para jogadores.")
        }
        val target =  Bukkit.getPlayer(args[0]) ?: return sender.sendMessage("§cJogador offline.")

        if (sender.name == target.name) {
            return sender.sendMessage("§cVocê não pode teleportar-se para sí mesmo.")
        }

        sender.player.teleport(target)
        sender.sendMessage("§7Você foi até §f${target.name}§7.")
    }
}