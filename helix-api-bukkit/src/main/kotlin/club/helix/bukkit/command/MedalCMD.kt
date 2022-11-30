package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.account.HelixMedal
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.util.HelixTimeData
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import java.util.concurrent.TimeUnit

class MedalCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf<CommandCompleter>().apply {
        val medals = if (sender.isConsole) HelixMedal.values().toMutableList() else sender.player.account.availableMedals()
        add(CommandCompleter(0, medals.map(HelixMedal::displayName).toMutableList()))
    }

    @CommandOptions(
        name = "medal",
        target = CommandTarget.PLAYER,
        description = "Selecionar medalha.",
        aliases = ["medalha"]
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val medals = sender.player.account.availableMedals()

        if (args.isEmpty()) {
            val text = TextComponent("§aMedalhas disponíveis: ")

            for (i in medals.indices) {
                val medal = medals[i]


                text.addExtra(TextComponent(
                    if (medal != HelixMedal.DEFAULT) "${medal.color}${medal.icon}"
                    else "${medal.color}${medal.displayName}"
                ).apply {
                    hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        ComponentBuilder("${medal.color}${medal.displayName}\n§7/medalha ${medal.displayName}").create())
                    clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/medal $medal")
                })

                if (i < medals.size.dec()) {
                    text.addExtra(TextComponent(", "))
                }
            }
            return sender.player.spigot().sendMessage(text)
        }

        val medal = HelixMedal.get(args.joinToString(" "))
            ?: return sender.sendMessage("§cMedalha não encontrada.")

        if (!medals.contains(medal)) {
            return sender.sendMessage("§cVocê não tem permissão para utilizar esta medalha.")
        }

        if (sender.player.account.medal == medal) {
            return sender.sendMessage("§cVocê já está utilizando esta medalha.")
        }

        if (HelixTimeData.getOrCreate(sender.name, "medal-cmd", 7, TimeUnit.SECONDS)) {
            return sender.sendMessage("§cAguarde ${HelixTimeData.getTimeFormatted(sender.name, "medal-cmd")} para alterar sua medalha novamente.")
        }

        sender.player.account.apply {
            this.medal = medal
            apiBukkit.components.userManager.saveAll(this)
        }
        sender.sendMessage("§eVocê selecionou a medalha ${medal.color}${medal.icon} ${medal.displayName}§e.")
    }
}