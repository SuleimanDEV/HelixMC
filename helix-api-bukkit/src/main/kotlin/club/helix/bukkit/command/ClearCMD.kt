package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ClearCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, Bukkit.getOnlinePlayers().map(Player::getName).toMutableList())
    )

    @CommandOptions(
        name = "clear",
        permission = true,
        description = "Limpar inventário dos jogadores."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty() && sender.isConsole)
            return sender.sendMessage("§cUtilize /clear <jogador> para limpar inventário dos jogadores.")

        Bukkit.getPlayer(if (args.isNotEmpty()) args.first() else sender.name)?.let {

            it.takeIf { sender.name == it.name || sender.hasPermission("helix.cmd.clear.other") }?.run {
                it.inventory.clear()
                it.inventory.armorContents = null
                it.updateInventory()
                sender.sendMessage(if (sender.name == it.name) "§6Seu inventário foi limpo!" else "§6O Inventário de §f${it.name} §6foi limpo!")

            } ?: sender.sendMessage("§cVocê não tem permissão para limpar inventário de terceiros.")

        } ?: sender.sendMessage("§cJogador offline.")
    }
}