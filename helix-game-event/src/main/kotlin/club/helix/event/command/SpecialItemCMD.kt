package club.helix.event.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.event.specialitem.SpecialItem

class SpecialItemCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, SpecialItem.values().map { it.toString().lowercase() }.toMutableList())
    )

    @CommandOptions(
        name = "specialitem",
        target = CommandTarget.PLAYER,
        permission = true,
        description = "Pegar itens especiais."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            return sender.sendMessage("§cUtilize /specialitem <id do item> para pegar um item especial.")
        }

        val item = SpecialItem.get(args[0]) ?: return sender.sendMessage("§cItem não encontrado.")

        sender.player.inventory.addItem(item.itemStack)
        sender.sendMessage("§aVocê pegou o item especial: ${item.toString().lowercase()}")
    }
}