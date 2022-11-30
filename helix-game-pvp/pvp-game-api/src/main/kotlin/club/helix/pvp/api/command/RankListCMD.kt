package club.helix.pvp.api.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.account.game.PvP
import club.helix.components.command.executor.CommandOptions

class RankListCMD: BukkitCommandExecutor() {

    @CommandOptions(
        name = "ranklist",
        description = "Listar os ranks.",
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        sender.sendMessage("§aLista de ranks:")

        sender.sendMessage(PvP.Rank.values().reversed().map {
            "  ${it.color}${it.symbol} ${it.displayName}"
        }.toTypedArray())
    }
}