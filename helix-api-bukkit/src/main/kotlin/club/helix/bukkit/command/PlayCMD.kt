package club.helix.bukkit.command

import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.server.HelixServer
import java.util.*

class PlayCMD: BukkitCommandExecutor() {
    companion object {
        private val playServers = mutableMapOf<String, AbstractMap.SimpleEntry<HelixServer, HelixServer.Category>>(
            Pair("pvp", AbstractMap.SimpleEntry(HelixServer.PVP, HelixServer.Category.LOBBY)),
            Pair("pvp-arena", AbstractMap.SimpleEntry(HelixServer.PVP, HelixServer.Category.PVP_ARENA)),
            Pair("pvp-fps", AbstractMap.SimpleEntry(HelixServer.PVP, HelixServer.Category.PVP_FPS)),
            Pair("pvp-lava", AbstractMap.SimpleEntry(HelixServer.PVP, HelixServer.Category.PVP_LAVA)),
            Pair("duels", AbstractMap.SimpleEntry(HelixServer.DUELS, HelixServer.Category.LOBBY)),
            Pair("duels-gladiator", AbstractMap.SimpleEntry(HelixServer.DUELS, HelixServer.Category.DUELS_GLADIATOR)),
            Pair("duels-uhc", AbstractMap.SimpleEntry(HelixServer.DUELS, HelixServer.Category.DUELS_UHC)),
            Pair("duels-sopa", AbstractMap.SimpleEntry(HelixServer.DUELS, HelixServer.Category.DUELS_SOUP)),
            Pair("duels-lava", AbstractMap.SimpleEntry(HelixServer.DUELS, HelixServer.Category.DUELS_LAVA)),
            Pair("duels-sumo", AbstractMap.SimpleEntry(HelixServer.DUELS, HelixServer.Category.DUELS_SUMO))
        )
    }

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, playServers.map { it.key }.toMutableList())
    )

    @CommandOptions(
        name = "play",
        target = CommandTarget.PLAYER,
        description = "Conectar diretamente nos minigames.",
        aliases = ["jogar"]
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            return sender.sendMessage(arrayOf(
                "§cServidores disponíveis:",
                playServers.map { it.key }.joinToString(", ") { "§c$it" }
            ))
        }

        val serverId = args[0]
        val server = playServers[serverId]
            ?: return sender.sendMessage("§cServidor inválido.")
        val availableServer = server.key.findAvailable(server.value)
            ?: return sender.sendMessage("§cNão há uma sala disponível.")

        sender.player.connect(availableServer, true)
    }
}