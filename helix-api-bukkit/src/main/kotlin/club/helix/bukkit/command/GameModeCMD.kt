package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player

class GameModeCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, Bukkit.getOnlinePlayers().map(Player::getName).toMutableList()),
        CommandCompleter(1, GameMode.values().map { it.name }.toMutableList())
    )

    @CommandOptions(
        name = "gamemode",
        permission = true,
        description = "Alterar modo de jogo dos jogadores.",
        aliases = ["gm"]
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty() || args.size == 1 && sender.isConsole)
            return sender.sendMessage(arrayOf(
                "§cUtilize /gamemode <modo de jogo> [jogador] para alterar o modo de jogo.",
                "§cModos disponíveis: ${StringUtils.join(GameMode.values(), ", ")}"
            ))

        lateinit var gamemode: GameMode

        when(args[0].lowercase()) {
            "0", "survival", "sobrevivencia", "s" -> gamemode = GameMode.SURVIVAL
            "1", "creative", "criativo", "c" -> gamemode = GameMode.CREATIVE
            "2", "adventure", "aventura", "a" -> gamemode = GameMode.ADVENTURE
            "3", "spectator", "espectador", "spec" -> gamemode = GameMode.SPECTATOR
            else -> return sender.sendMessage("§cModo de jogo não encontrado. \nModos disponíveis: ${StringUtils.join(GameMode.values(), ", ")}")
        }

        val target = Bukkit.getPlayer(if (args.size == 2) args[1] else sender.name)
            ?: return sender.sendMessage("§cJogador offline.")

        if (sender.name != target.name && !sender.hasPermission("helix.cmd.gamemode.other")) {
            return sender.sendMessage("§cVocê não tem permissão para alterar o modo de jogo de terceiros.")
        }

        target.gameMode = gamemode
        sender.sendMessage(if (sender.name == target.name) "§6Modo de jogo alterado para §f§l$gamemode§6." else
            "§6Modo de jogo de §f${target.name} §6alterado para §f§l$gamemode§6.")
    }
}