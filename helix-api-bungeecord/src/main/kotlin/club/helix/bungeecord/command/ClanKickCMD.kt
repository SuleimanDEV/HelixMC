package club.helix.bungeecord.command

import club.helix.components.clan.ClanManager
import club.helix.components.clan.ClanRole
import club.helix.components.clan.exception.ClanMemberNotFoundException
import club.helix.bungeecord.kotlin.clan.broadcast
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.ComponentBuilder

class ClanKickCMD(
    private val clanManager: ClanManager,
    private val sender: BungeeCommandSender,
    private val args: Array<String>
) {
    fun execute() {
        val clan = clanManager.getClanByMember(sender.name)
            ?: return sender.message(ComponentBuilder("Você não possui um clan.")
                .color(ChatColor.RED).create())
        val member = clan.getMember(sender.name)
            ?: return sender.message(ComponentBuilder("Ocorreu um erro ao carregar sua patente.")
                .color(ChatColor.RED).create())

        if (args.size == 1) {
            return sender.message(ComponentBuilder("Utilize /clan expulsar <membro>.")
                .color(ChatColor.RED).create())
        }

        if (member.role != ClanRole.LEADER && member.role != ClanRole.MANAGER) {
            return sender.message(ComponentBuilder("Apenas o líder e gerentes podem expulsar membros.")
                .color(ChatColor.RED).create())
        }

        val target = clan.getMember(args[1])
            ?: return sender.message(ComponentBuilder("Este jogador não faz parte do clan.")
                .color(ChatColor.RED).create())

        if (target.role != ClanRole.MEMBER) {
            return sender.message(ComponentBuilder("Você não pode remover este membro.")
                .color(ChatColor.RED).create())
        }

        try {
            clan.removeMember(target)
            clanManager.controller.save(clan)
            clan.broadcast(ComponentBuilder("§b§lCLAN §7» ")
                .append("${target.name} foi removido por ${sender.name}.").color(ChatColor.RED)
                .create())

            ProxyServer.getInstance().getPlayer(target.name)?.apply {
                sendMessage(*ComponentBuilder("§b§lCLAN §7» ")
                    .append("Você foi removido do clan ${clan.name} por ${sender.name}.").color(ChatColor.RED)
                    .create())
            }
        }catch (error: ClanMemberNotFoundException) {
            return sender.message(ComponentBuilder("Ocorreu um erro ao sair do clan. (ClanMemberNotFound)")
                .color(ChatColor.RED).create())
        }catch(error: Exception) {
            error.printStackTrace()
            return sender.message(ComponentBuilder("Ocorreu um erro ao sair do clan. (Internal Error)")
                .color(ChatColor.RED).create())
        }
    }
}