package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.inventory.report.ReportsInventory
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget

class ReportsCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    @CommandOptions(
        name = "reports",
        target = CommandTarget.PLAYER,
        description = "Visualizar denúncias.",
        aliases = ["reportes", "denuncias"]
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (!sender.hasPermission("helix.report")) {
            return sender.sendMessage("§cVocê não tem permissão para executar este comando.")
        }

        val reports = apiBukkit.components.reportController.load().takeIf {
            it.isNotEmpty()
        } ?: return sender.sendMessage("§cNão há denúncias.")

        ReportsInventory(apiBukkit.components, reports).open(sender.player)
    }


}