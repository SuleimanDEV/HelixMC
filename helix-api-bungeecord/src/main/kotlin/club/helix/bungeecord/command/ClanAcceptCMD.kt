package club.helix.bungeecord.command

import club.helix.components.clan.ClanManager
import club.helix.bungeecord.kotlin.clan.broadcast
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder

class ClanAcceptCMD(
    private val clanManager: ClanManager,
    private val sender: BungeeCommandSender,
    private val args: Array<String>
) {
    fun execute() {
        if (clanManager.containsClanByName(sender.name)) {
            return sender.message(ComponentBuilder("Você já possui um clan.")
                .color(ChatColor.RED).create())
        }

        val clanTag = args[1]
        val clan = clanManager.getClanByTag(clanTag)
            ?: return sender.message(ComponentBuilder("Clan não encontrado.")
                .color(ChatColor.RED).create())

        if (!clanManager.hasPendingInvite(sender.name, clan)) {
            clanManager.deleteInvite(sender.name, clan)
            return sender.message(ComponentBuilder("Você não possui um convite para este clan,")
                .color(ChatColor.RED).create())
        }

        if (clan.members.size >= clan.maxMembers) {
            return sender.message(ComponentBuilder("Este clan atingiu o limite de membros..")
                .color(ChatColor.RED).create())
        }

        clanManager.deleteInvite(sender.name, clan)
        clan.addMember(sender.name)

        clan.broadcast(ComponentBuilder("§b§lCLAN §8» ")
            .append("${sender.name} entrou para o clan.").color(ChatColor.GREEN)
            .color(ChatColor.RED).create())
        clanManager.controller.save(clan)
    }
}