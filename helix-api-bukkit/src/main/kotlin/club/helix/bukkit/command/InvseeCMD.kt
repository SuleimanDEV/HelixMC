package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class InvseeCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, Bukkit.getOnlinePlayers().map(Player::getName).toMutableList())
    )

    @CommandOptions(
        name = "invsee",
        target = CommandTarget.PLAYER,
        permission = true,
        description = "Visualizar inventário de um jogador."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            return sender.sendMessage("§cUtilize /invsee <jogador> para ver o inventário do mesmo.")
        }
        val target = Bukkit.getPlayer(args[0])
            ?: return sender.sendMessage("§cJogador offline.")

        sender.player.openInventory(target.inventory)
    }
}