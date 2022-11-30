package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.server.HelixServer
import club.helix.components.util.HelixTimeFormat

class OnlineTimeCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, HelixServer.networkPlayers)
    )

    @CommandOptions(
        name = "onlinetime",
        description = "Ver tempo online."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (sender.isConsole && args.isEmpty()) {
            return sender.sendMessage("§cUtilize /onlinetime <jogador> para ver o tempo online.")
        }

        val userManager = apiBukkit.components.userManager
        val targetName = if (args.isEmpty()) sender.name else args[0]
        val target = (userManager.getUser(targetName)
            ?: userManager.redisController.load(targetName)
            ?: userManager.userSqlController.load(targetName))
            ?: return sender.sendMessage("§cJogador não registrado.")
        val me = targetName == sender.name

        if (target.onlineTime <= 0) {
            return sender.sendMessage(if (me)
                "§eVocê é novato por aqui, jogue em nossos minigames para juntar horas jogadas."
            else "§cEste jogador não possui horas jogadas.")
        }

        val onlineTime = HelixTimeFormat.format(target.onlineTime)
        sender.sendMessage(if (me) "§eTempo de jogo: §f$onlineTime" else
            "§eTempo de jogo de $targetName: §f$onlineTime")
    }
}