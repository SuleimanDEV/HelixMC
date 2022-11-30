package club.helix.bungeecord.command

import club.helix.bungeecord.HelixBungee
import club.helix.bungeecord.kotlin.player.account
import club.helix.bungeecord.precommand.executor.BungeeCommandExecutor
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import club.helix.components.account.HelixReport
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.util.HelixTimeData
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.concurrent.TimeUnit

class ReportCMD(private val apiBungee: HelixBungee): BungeeCommandExecutor() {

    override fun onTabComplete(sender: BungeeCommandSender) = mutableListOf(
        CommandCompleter(0, apiBungee.proxy.players.map(ProxiedPlayer::getName).toMutableList())
    )

    @CommandOptions(
        name = "report",
        target = CommandTarget.PLAYER,
        description = "Denunciar jogador.",
        aliases = ["reportar", "denunciar", "reporthack"]
    )
    override fun execute(sender: BungeeCommandSender, args: Array<String>) {
        if (sender.isPlayer && args.size == 1 && args[0].lowercase() == "toggle" && sender.hasPermission("helix.report")) {
            val user = sender.player.account.apply {
                preferences.staff.notifyReports = !preferences.staff.notifyReports
                apiBungee.components.userManager.saveAll(this)
            }
            return sender.message(ComponentBuilder(
                if (user.preferences.staff.notifyReports) "Você ativou as notificações das denúncias."
                    else "Você desativou as notificações das denúncias."
            ).color(if (user.preferences.staff.notifyReports) ChatColor.GREEN else ChatColor.RED).create())
        }

        if (args.size < 2) {
            return sender.message(ComponentBuilder(
                "Utilize /reportar <jogador> <motivo> para denunciar um infrator."
            ).color(ChatColor.RED).create())
        }
        val target = apiBungee.proxy.getPlayer(args[0])
            ?: return sender.sendMessage("§cJogador offline.")

        if (sender.name == target.name) {
            return sender.sendMessage("§cVocê não pode reportar sí mesmo.")
        }

        if (HelixTimeData.getOrCreate(sender.name, "report-cmd", 13, TimeUnit.SECONDS)) {
            return sender.sendMessage("§cAguarde ${HelixTimeData.getTimeFormatted(sender.name, "report-cmd")} para denunciar um jogador novamente.")
        }

        val reasonBuilder = StringBuilder()
        for (i in 1 until args.size) {
            reasonBuilder.append(args[i]).append(" ")
        }

        val reason = reasonBuilder.toString().trim()
        val report = apiBungee.components.reportController.load(target.name)
            ?: HelixReport(target.name)

        report.insert(sender.name, reason)
        apiBungee.components.reportController.save(report)

        sender.sendMessage("§aVocê reportou $target por §f$reason§a.")
        if (report.priority.ordinal >= HelixReport.Priority.HIGH.ordinal) {
            sender.message(
                ComponentBuilder("O acusado está como prioridade ").color(ChatColor.YELLOW)
                    .append(report.priority.displayName)
                    .append(" em nosso sistema.").color(ChatColor.YELLOW)
                .create()
            )
        }

        apiBungee.proxy.players.filter {
            it.account.let { user ->
                user.hasPermission("helix.report") && user.preferences.staff.notifyReports
            }
        }.forEach {
            apiBungee.proxy.createTitle()
                .title(*ComponentBuilder("").create())
                .subTitle(*ComponentBuilder(
                    "§cVocê possui uma nova denúncia!"
                ).color(ChatColor.GOLD).create())
                .fadeIn(20)
                .fadeOut(20)
                .stay(20)
                .send(it)

            val clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports")

            it.sendMessage(*ComponentBuilder("Você possui uma nova denúncia.\n")
                .color(ChatColor.GOLD).event(clickEvent)
                .append("Prioridade: ${report.priority.displayName}").color(ChatColor.GOLD)
                .event(clickEvent)
                .create())
        }
    }
}