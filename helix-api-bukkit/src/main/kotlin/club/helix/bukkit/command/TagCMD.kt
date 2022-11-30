package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.nms.NameTagNMS
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.account.HelixRank
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.util.HelixTimeData
import java.util.concurrent.TimeUnit

class TagCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf<CommandCompleter>().apply {
        add(
            CommandCompleter(0,
            HelixRank.values().filter {
                sender.isConsole || sender.player.account.availableTags().contains(it)
            }.map { it.displayName }.toMutableList())
        )
    }

    @CommandOptions(
        name = "tag",
        description = "Alterar tag",
        target = CommandTarget.PLAYER
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val user = sender.player.account

        if (args.isEmpty()) {
            return sender.sendMessage("§eSuas tags: ${user.availableTags().joinToString("§f, ") { it.coloredName }}")
        }

        val tag = HelixRank.get(args.joinToString(" "))
            ?: return sender.sendMessage("§cTag não encontrada.")

        if (user.tag == tag) {
            return sender.sendMessage("§cVocê já está utilizando esta tag.")
        }

        if (!user.availableTags().contains(tag)) {
            return sender.sendMessage("§cVocê não tem permissão para utilizar esta tag.")
        }

        if (HelixTimeData.getOrCreate(sender.name, "tag-cmd", 7, TimeUnit.SECONDS)) {
            return sender.sendMessage("§cAguarde ${HelixTimeData.getTimeFormatted(sender.name, "tag-cmd")} para alterar sua tag novamente.")
        }

        user.tag = tag
        apiBukkit.components.userManager.saveAll(user, false)
        NameTagNMS.setNametag(sender.player, tag, NameTagNMS.Reason.TAG)
        sender.sendMessage("§6Sua tag foi atualizada!")
    }
}