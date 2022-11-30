package club.helix.event.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.event.EventPlugin
import club.helix.event.player.GamePlayerType

class TpAllCMD(private val plugin: EventPlugin): BukkitCommandExecutor() {

    @CommandOptions(
        name = "tpall",
        permission = true,
        target = CommandTarget.PLAYER,
        description = "Puxar todos os jogadores."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val players = plugin.game.getPlayers(GamePlayerType.PLAYING).takeIf { it.isNotEmpty() }
            ?: return sender.sendMessage("§cNão há jogadores para puxar.")

        players.forEach { it.player.teleport(sender.player) }
        sender.sendMessage("§aVocê puxou ${players.size} ${if (players.size > 1) "jogadores" else "jogador"}.")
    }
}