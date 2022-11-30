package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class RestoreLifeCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, Bukkit.getOnlinePlayers().map(Player::getName).toMutableList())
    )

    @CommandOptions(
        name = "restorelife",
        permission = true,
        description = "Restaurar vida dos jogadores.",
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty() && sender.isConsole)
            return sender.sendMessage("§cUtilize /restorelife <jogador> para restaurar a vida dos jogadores.")

        val target = Bukkit.getPlayer(if (args.isEmpty()) sender.name else args[0])
            ?: return sender.sendMessage("§cJogador offline.")

        if (target.health == target.maxHealth) {
            return sender.sendMessage("§cEste jogador está com a vida cheia.")
        }

        target.apply { health = maxHealth }
        sender.sendMessage(if (sender.name == target.name) "§3Sua vida foi restaurada!" else
            "§3A vida de §f${target.name} §3foi restaurada!")
    }
}