package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class RestoreHungerCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, Bukkit.getOnlinePlayers().map(Player::getName).toMutableList())
    )

    @CommandOptions(
        name = "restorehunger",
        permission = true,
        description = "Restaurar fome dos jogadores."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty() && sender.isConsole)
            return sender.sendMessage("§cUtilize /restorehunger <jogador> para restaurar a fome dos jogadores.")

        val target = Bukkit.getPlayer(if (args.isEmpty()) sender.name else args[0])
            ?: return sender.sendMessage("§cJogador offline.")

        if (target.foodLevel >= 20) {
            return sender.sendMessage("§cEste jogador está com a fome cheia.")
        }

        target.foodLevel = 20
        sender.sendMessage(if (sender.name == target.name) "§eSua fome foi restaurada!" else
            "§eA fome de §f${target.name} §efoi restaurada!")
    }
}