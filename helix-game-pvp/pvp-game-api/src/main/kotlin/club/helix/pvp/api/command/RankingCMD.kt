package club.helix.pvp.api.command

import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import com.google.common.base.Strings
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class RankingCMD: BukkitCommandExecutor() {

    private fun progressBar(currentXp: Int, maxXp: Int): String {
        val percent = (currentXp.toDouble() / maxXp).toFloat()
        val totalBars = 11
        val progressBars = (totalBars * percent).toInt()
        val separator = "∎"

        return StringBuilder().run {
            progressBars.takeIf { it > 0 }?.let {
                append("§a${Strings.repeat(separator, it)}")
            }
            (totalBars - progressBars).takeIf { it > 0 }?.let {
                append("§7${Strings.repeat(separator, it)}")
            }
            toString()
        }
    }

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, Bukkit.getOnlinePlayers().map(Player::getName).toMutableList())
    )

    @CommandOptions(
        name = "ranking",
        description = "Ver o ranking.",
        aliases = ["rank"]
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (sender.isConsole && args.isEmpty()) {
            return sender.sendMessage("§cUtilize /ranking <jogador> para ver o rank.")
        }

        val targetName = if (args.isEmpty()) sender.name else args[0]
        val target = Bukkit.getPlayer(targetName)?.account
            ?: return sender.sendMessage("§cJogador offline.")

        sender.sendMessage("§a${if (target.name == sender.name) "Seu ranking" else "Ranking de ${target.name}"}:")

        target.pvp.nextRank()?.let {
            val rank = target.pvp.rank

            val prefix = "${rank.color}${rank.displayName} ${target.pvp.romanNumeral(target.pvp.subrank)}"
            val progress = "${progressBar(target.pvp.exp, target.pvp.expToUp)} §e${target.pvp.exp}/${target.pvp.expToUp.decimalFormat()} xp"
            val suffix = "${it.key.color}${it.key.displayName} ${target.pvp.romanNumeral(it.value)}"

            sender.sendMessage("$prefix $progress $suffix")

        } ?: sender.sendMessage("§cVocê já está no último rank!")
    }
}