package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class StrikeLightningCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, Bukkit.getOnlinePlayers().map(Player::getName).toMutableList())
    )

    @CommandOptions(
        name = "strikelightning",
        permission = true,
        description = "Lançar raios.",
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty() && sender.isConsole)
            return sender.sendMessage("§cUtilize /strikelightning <jogador> para lançar raios nos jogadores.")

        val target = Bukkit.getPlayer(if (args.isEmpty()) sender.name else args[0])
            ?: return sender.sendMessage("§cJogador offline.")

        target.world.strikeLightningEffect(target.location)
        sender.sendMessage(if (sender.name == target.name) "§eRaios foram lançados em sua localização." else
            "§eRaios foram lançados na localização de §f${target.name}§e.")
    }
}