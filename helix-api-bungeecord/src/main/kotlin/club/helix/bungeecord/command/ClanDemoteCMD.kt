package club.helix.bungeecord.command

import club.helix.components.clan.ClanManager
import club.helix.components.clan.ClanRole
import club.helix.bungeecord.kotlin.clan.broadcast
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder

class ClanDemoteCMD(
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


        if (member.role != ClanRole.LEADER) {
            return sender.message(ComponentBuilder("Apenas o líder pode rebaixar membros.")
                .color(ChatColor.RED).create())
        }

        if (args.size == 1) {
            return sender.message(ComponentBuilder("Utilize /clan rebaixar <membro>.")
                .color(ChatColor.RED).create())
        }

        val target = clan.getMember(args[1])
            ?: return sender.message(ComponentBuilder("Este jogador não faz parte do clan.")
                .color(ChatColor.RED).create())

        if (target.role != ClanRole.MANAGER) {
            return sender.message(ComponentBuilder("Você só pode rebaixar gerentes.")
                .color(ChatColor.RED).create())
        }

        target.role = ClanRole.MEMBER
        clanManager.controller.save(clan)

        clan.broadcast(ComponentBuilder("§d§lCLAN §8» ")
            .append("${target.name} foi rebaixado para ${ClanRole.MEMBER.color}Membro§c.").color(ChatColor.RED)
            .create())
    }
}