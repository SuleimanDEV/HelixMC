package club.helix.bungeecord.command

import club.helix.components.clan.ClanManager
import club.helix.components.clan.ClanRole
import club.helix.components.util.HelixTimeData
import club.helix.bungeecord.kotlin.clan.broadcast
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import java.util.concurrent.TimeUnit

class ClanDeleteCMD(
    private val clanManager: ClanManager,
    private val sender: BungeeCommandSender,
) {
    fun execute() {
        val clan = clanManager.getClanByMember(sender.name)
            ?: return sender.message(ComponentBuilder("Você não possui um clan.")
                .color(ChatColor.RED).create())

        clan.getMember(sender.name)?.takeIf { it.role != ClanRole.LEADER }?.also {
            return sender.message(ComponentBuilder("Apenas o líder pode deletar o clan.")
                .color(ChatColor.RED).create())
        }

        if (HelixTimeData.isActive(sender.name, "delete-clan")) {
            clan.broadcast(ComponentBuilder("O clan ${clan.name} foi deletado!")
                .color(ChatColor.RED).create())
            clanManager.unregister(clan)
            clanManager.controller.delete(clan)
            return sender.message(ComponentBuilder("Você deletou o clan!")
                .color(ChatColor.RED).create())
        }

        HelixTimeData.putTime(sender.name, "delete-clan", 5, TimeUnit.SECONDS)

        sender.message(ComponentBuilder("§c§lCLAN §8» ")
            .append("Você tem certeza que deseja deletar o clan?\n").color(ChatColor.YELLOW)
            .append("§c§lCLAN §8» ")
            .append("Execute o comando novamente para confirmar.").color(ChatColor.YELLOW)
            .create())
    }
}