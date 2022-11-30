package club.helix.event.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.fetcher.StringFetcher
import club.helix.components.util.HelixTimeFormat
import club.helix.event.EventGame
import club.helix.event.EventPlugin

class SetTimeCMD(private val plugin: EventPlugin): BukkitCommandExecutor() {

    @CommandOptions(
        name = "settime",
        permission = true,
        description = "Alterar tempo do evento."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (plugin.game.state != EventGame.State.WAITING) {
            return sender.sendMessage("§cVocê só pode alterar o tempo no modo de espera.")
        }

        if (args.isEmpty()) {
            return sender.sendMessage("§cUtilize /settime <tempo> para alterar o tempo.")
        }

        val time = StringFetcher.getNumbers(args[0])?.toLongOrNull()?.takeIf { it != 0L }
            ?: return sender.sendMessage("§cNúmero inválido.")

        val timeUnit = HelixTimeFormat.getTimeUnit(StringFetcher.getLetters(args[0]))
            ?: return sender.sendMessage("§cTempo inválido.")

        if (timeUnit.toHours(time) > 1) {
            return sender.sendMessage("§cO tempo limite é de 1 hora.")
        }

        plugin.game.time = timeUnit.toSeconds(time).toInt()
        sender.sendMessage("§6Tempo alterado: §f${HelixTimeFormat.format(timeUnit.toMillis(time))}")
    }
}