package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.World

class TimeCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, Bukkit.getWorlds().map(World::getName).toMutableList())
    )

    @CommandOptions(
        name = "time",
        permission = true,
        description = "Alterar o tempo do mundo."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty() || args.size < 2 && sender.isConsole)
            return sender.sendMessage("§cUtilize /time <valor> [world] para alterar o tempo do mundo.")

        val time = args.firstOrNull() ?: return sender.sendMessage("§cTempo inválido.")
        val world = (if (args.size >= 2) Bukkit.getWorld(args[1]) else Bukkit.getWorlds().firstOrNull())
            ?: return sender.sendMessage("§cMundo inválido.\n§cMundos disponíveis: ${StringUtils.join(Bukkit.getWorlds().map { it.name }, ", ")}")

        world.time = time.toLong()
        sender.sendMessage("§bTempo de §f${world.name} §balterado: §f$time")
    }
}