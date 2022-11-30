package club.helix.event.command

import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.event.EventGame
import club.helix.event.EventPlugin
import club.helix.event.scoreboard.WaitingScoreboard
import club.helix.event.scoreboard.WhitelistScoreboard
import org.bukkit.Bukkit

class WhitelistCMD(private val plugin: EventPlugin): BukkitCommandExecutor() {

    @CommandOptions(
        name = "whitelist",
        permission = true,
        description = "Gerenciar whitelist."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            return sender.sendMessage("§cUtilize /whitelist <on/off> para gerenciar a whitelist.")
        }

        val argument = args[0].takeIf { it.lowercase() == "on" || it.lowercase() == "off" }?.lowercase()
            ?: return sender.sendMessage("§cArgumento inválido.")
        val whitelist = argument == "on"

        val isWhitelisted = plugin.game.state == EventGame.State.WHITELIST
        if (whitelist == isWhitelisted) {
            return sender.sendMessage("§cA whitelist já está ${if (whitelist) "ativada" else "desativada"}.")
        }

        val state = if (whitelist) EventGame.State.WHITELIST else EventGame.State.WAITING
        val scoreboard = if (whitelist) WhitelistScoreboard(plugin.game) else WaitingScoreboard(plugin.game)

        plugin.game.changeState(state)
        plugin.game.changeScoreboard(scoreboard)

        plugin.notifyPlayers.forEach {
            it.sendMessage("${if (whitelist) "§a" else "§c"}${sender.name} ${if (whitelist) "ativou" else "desativou"} a whitelist.")
        }

        if (whitelist) {
            val availableLobby = plugin.apiBukkit.currentServer.findAvailableLobby()

            Bukkit.getOnlinePlayers().filter {
                !plugin.notifyPlayers.contains(it)
            }.forEach {
                if (availableLobby == null) {
                    return@forEach it.kickPlayer("§cO evento foi fechado!")
                }
                it.sendMessage("§6O evento foi fechado!")
                it.connect(availableLobby)
            }
        }
    }
}