package club.helix.lobby.duels.command

import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.HelixComponents
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.server.HelixServer
import club.helix.duels.api.DuelsRequest
import club.helix.duels.api.DuelsRequestController

class SpectateCMD(private val api: HelixComponents): BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, HelixServer.DUELS.onlinePlayers)
    )

    @CommandOptions(
        name = "espectar",
        target = CommandTarget.PLAYER,
        description = "Espectar jogadores.",
        aliases = ["spec"]
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            return sender.sendMessage("§cUtilize /espectar <jogador> para espectar.")
        }

        val target = args[0]
        val targetServer = HelixServer.getPlayerServer(target)
            ?: return sender.sendMessage("§cEste jogador está offline ou não está jogando Duels.")

        if (targetServer.type != HelixServer.DUELS || targetServer.category == HelixServer.Category.LOBBY) {
            return sender.sendMessage("§cEste jogador não está em uma partida.")
        }

        DuelsRequestController(api).save(DuelsRequest(
            sender.name,
            "SPECTATE",
            mutableMapOf(
                Pair("target", target)
            )
        ))

        sender.sendMessage("§aPartida encontrada!")
        sender.player.connect(targetServer, true)
    }
}