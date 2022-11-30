package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class SetMaxHealthCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, Bukkit.getOnlinePlayers().map(Player::getName).toMutableList())
    )

    @CommandOptions(
        name = "setmaxhealth",
        permission = true,
        description = "Alterar a vida máxima dos jogadores.",
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty() || args.size < 2 && sender.isConsole)
            return sender.sendMessage("§cUtilize /setmaxhealth <jogador> <vida> para alterar a vida máxima dos jogadores.")

        Bukkit.getPlayer(if (args.size > 1) args[0] else sender.name)?.let {

            val health = (if (args.size > 1) args[1] else args[0]).toIntOrNull()?.apply {
                if (this < 1 || this > 500) return sender.sendMessage("§cUtilize um valor entre 1 e 500.")
            } ?: return sender.sendMessage("§cValor inválido.")

            it.maxHealth = health.toDouble()
            sender.sendMessage(if (sender.name == it.name)
                "§aSua vida máxima foi alterada para §f$health§a." else
                "§aA vida máxima do jogador §a${it.name} §afoi alterada para §f$health§a.")

        }  ?: run { sender.sendMessage("§cJogador offline.") }
    }
}