package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.account.HelixMedal
import club.helix.components.account.HelixUserLogin
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.server.HelixServer
import club.helix.components.util.HelixTimeFormat
import java.text.SimpleDateFormat

class PInfoCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, HelixServer.networkPlayers)
    )

    @CommandOptions(
        name = "pinfo",
        permission = true,
        description = "Visualizar informações privadas de um jogador."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            return sender.sendMessage("§cUtilize /pinfo <jogador> para visualizar suas informações privadas.")
        }

        val userManager = apiBukkit.components.userManager
        val targetName = args[0]
        val target = (userManager.getUser(targetName)
            ?: userManager.redisController.load(targetName)
            ?: userManager.userSqlController.load(targetName))
            ?: return sender.sendMessage("§cEste jogador não está registrado.")
        val dateFormat = SimpleDateFormat("dd/MM/yyy HH:mm")
        val rankLife = target.mainRankLife

        sender.sendMessage(arrayOf(
            "§7Informações de §f$targetName§7:",
            "§7Rank: ${rankLife.rank.color}${rankLife.rank.displayName} §e(${rankLife.formatTime()})",
            "§7Tag: ${target.tag.color}${target.tag.displayName}",
            "§7Tempo online: ${HelixTimeFormat.format(target.onlineTime)}",
            "§7Tipo de conta: ${if (target.login.type == HelixUserLogin.Type.PREMIUM) "§6Original" else "§cPirata"}",
            "§7Medalha: ${target.medal.color}${target.medal.takeIf { it != HelixMedal.DEFAULT }?.run { "$icon $displayName" } ?: target.medal.displayName}",
            "§7Status: ${HelixServer.getPlayerServer(targetName)?.run { "§aOnline §e(${displayName} - $serverName)" } ?: "§cOffline"}",
            "",
            "§7Registro: §f${dateFormat.format(target.login.firstLogin)}",
            "§7Ultimo login: §f${dateFormat.format(target.login.lastLogin)}"
        ))
    }
}