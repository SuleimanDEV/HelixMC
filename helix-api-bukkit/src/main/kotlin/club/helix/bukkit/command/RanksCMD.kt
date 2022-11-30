package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.server.HelixServer

class RanksCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) =
        mutableListOf(CommandCompleter(0, HelixServer.networkPlayers))

    @CommandOptions(
        name = "ranks",
        description = "Ver seus ranks ativos."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (sender.isConsole && args.isEmpty()) {
            return sender.sendMessage("§cUtilize /ranks <jogador> para ver os ranks de um jogador.")
        }

        val targetName = if (args.isEmpty()) sender.name else args[0]

        if (targetName.lowercase() != sender.name.lowercase() && !sender.hasPermission("helix.cmd.ranks.other")) {
            return sender.sendMessage("§cVocê não tem permissão para ver o rank de outros jogadores.")
        }

        val userManager = apiBukkit.components.userManager
        val target = userManager.getUser(targetName)
            ?: userManager.redisController.load(targetName)
            ?: userManager.userSqlController.load(targetName)
            ?: return sender.sendMessage("§cJogador não registrado.")

        sender.sendMessage(arrayOf(
            "§aRanks de ${target.name}:",
            *target.ranksLife.sortedBy { it.rank.ordinal }.map {
                "§8- ${it.rank.color}${it.rank.displayName} §e(${it.formatTime()})"
            }.toTypedArray()
        ))
    }
}