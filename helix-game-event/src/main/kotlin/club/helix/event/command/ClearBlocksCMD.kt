package club.helix.event.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.event.EventPlugin
import org.bukkit.Material

class ClearBlocksCMD(private val plugin: EventPlugin): BukkitCommandExecutor() {

    @CommandOptions(
        name = "clearblocks",
        permission = true,
        description = "Limpar blocos."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val blocks = plugin.game.blocks.takeIf { it.isNotEmpty() }
            ?: return sender.sendMessage("§cNão há blocos para remover.")

        blocks.forEach { it.type = Material.AIR }
        blocks.clear()
        sender.sendMessage("§aBlocos removidos: ${blocks.size}")
    }
}