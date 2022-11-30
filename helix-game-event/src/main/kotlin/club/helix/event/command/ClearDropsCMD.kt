package club.helix.event.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.event.EventPlugin
import org.bukkit.entity.Entity
import org.bukkit.entity.Item

class ClearDropsCMD(private val plugin: EventPlugin): BukkitCommandExecutor() {

    @CommandOptions(
        name = "cleardrops",
        permission = true,
        description = "Limpar drops."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val drops = plugin.server.worlds.flatMap { it.entities.filterIsInstance<Item>() }.takeIf { it.isNotEmpty() }
            ?: return sender.sendMessage("§cNão há drops para remover.")

        drops.forEach(Entity::remove)
        sender.sendMessage("§aDrops removidos: ${drops.size}")
    }
}