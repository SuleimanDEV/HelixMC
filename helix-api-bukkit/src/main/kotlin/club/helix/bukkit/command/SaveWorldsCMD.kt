package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import org.bukkit.Bukkit
import org.bukkit.World

class SaveWorldsCMD: BukkitCommandExecutor() {

    @CommandOptions(
        name = "saveworlds",
        permission = true,
        description = "Salvar mundos."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val worlds = Bukkit.getWorlds().onEach(World::save)

        sender.sendMessage("§aNo total foram ${worlds.size} ${if (worlds.size > 1) "mundos" else "mundo"} salvos.")
    }
}