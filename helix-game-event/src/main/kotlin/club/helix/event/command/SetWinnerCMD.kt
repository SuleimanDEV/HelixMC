package club.helix.event.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.event.EventGame
import club.helix.event.EventPlugin
import club.helix.event.event.GameEndEvent
import club.helix.event.player.GamePlayerType
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class SetWinnerCMD(private val plugin: EventPlugin): BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, Bukkit.getOnlinePlayers().map(Player::getName).toMutableList())
    )

    @CommandOptions(
        name = "setwinner",
        permission = true,
        description = "Setar vencedor do evento."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (plugin.game.state != EventGame.State.PLAYING) {
            return sender.sendMessage("§cVocê não pode setar o vencedor neste estado. (${plugin.game.state})")
        }

        if (args.isEmpty()) {
            return sender.sendMessage("§cUtilize /setwinner <jogador> para definir um vencedor.")
        }

        val winner = Bukkit.getPlayer(args[0])?.let {
            plugin.game.getPlayer(it, GamePlayerType.PLAYING)
        } ?: return sender.sendMessage("§cJogador inválido.")

        plugin.game.changeState(EventGame.State.ENDED)
        Bukkit.getPluginManager().callEvent(GameEndEvent(plugin.game, winner))

        plugin.notifyPlayers.forEach {
            it.sendMessage("§a${sender.name} definiu um vencedor: ${winner.player.name}")
        }
    }
}