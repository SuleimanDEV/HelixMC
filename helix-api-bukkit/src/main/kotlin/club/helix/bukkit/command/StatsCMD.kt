package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.inventory.StatisticInventory
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.server.HelixServer
import club.helix.components.util.HelixTimeData
import java.util.concurrent.TimeUnit

class StatsCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, HelixServer.networkPlayers)
    )

    @CommandOptions(
        name = "stats",
        target = CommandTarget.PLAYER,
        description = "Visualizar estatísticas dos jogadores.",
        aliases = ["status"]
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (HelixTimeData.getOrCreate(sender.name, "stats-cmd", 10, TimeUnit.SECONDS)) {
            return sender.sendMessage("§cAguarde ${HelixTimeData.getTimeFormatted(sender.name, "stats-cmd")} para ver os status novamente.")
        }

        val targetName = if (args.isEmpty()) sender.name else args[0]

        val userManager = apiBukkit.components.userManager
        val target = (userManager.redisController.load(targetName)
            ?: userManager.userSqlController.load(targetName))
            ?: return sender.sendMessage("§cJogador não registrado.")

        StatisticInventory(target).open(sender.player)
    }
}