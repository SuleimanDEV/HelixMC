package club.helix.bungeecord.command

import club.helix.components.clan.ClanManager
import club.helix.components.clan.ClanRole
import club.helix.bungeecord.kotlin.clan.broadcast
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder

class ClanPromoteCMD(
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
            return sender.message(ComponentBuilder("Apenas o líder pode promover membros.")
                .color(ChatColor.RED).create())
        }

        if (args.size == 1) {
            return sender.message(ComponentBuilder("Utilize /clan promover <membro>.")
                .color(ChatColor.RED).create())
        }

        val target = clan.getMember(args[1])
            ?: return sender.message(ComponentBuilder("Este jogador não faz parte do clan.")
                .color(ChatColor.RED).create())

        if (target.role != ClanRole.MEMBER) {
            return sender.message(ComponentBuilder("Você não pode promover este membro.")
                .color(ChatColor.RED).create())
        }

        target.role = ClanRole.MANAGER
        clanManager.controller.save(clan)
        clan.broadcast(ComponentBuilder("§b§lCLAN §8» ")
            .append("${target.name} foi promovido para ${ClanRole.MANAGER.color}Gerente§e.").color(ChatColor.YELLOW)
            .create()
        )
    }
}