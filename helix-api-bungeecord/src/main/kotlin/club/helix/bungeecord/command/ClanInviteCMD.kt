package club.helix.bungeecord.command

import club.helix.bungeecord.kotlin.clan.broadcast
import club.helix.bungeecord.kotlin.player.account
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import club.helix.components.clan.ClanManager
import club.helix.components.clan.ClanRole
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import java.util.concurrent.TimeUnit

class ClanInviteCMD(
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

        if (member.role != ClanRole.LEADER && member.role != ClanRole.MANAGER) {
            return sender.message(ComponentBuilder("Apenas o líder e gerentes podem convidar membros.")
                .color(ChatColor.RED).create())
        }

        if (args.size == 1) {
            return sender.message(ComponentBuilder("Utilize /clan convidar <membro>.")
                .color(ChatColor.RED).create())
        }

        val targetName = args[1]
        val target = ProxyServer.getInstance().getPlayer(targetName)
            ?: return sender.message(ComponentBuilder("Jogador offline.")
                .color(ChatColor.RED).create())

        if (clanManager.containsClanByName(targetName)) {
            return sender.message(ComponentBuilder("Este jogador já possui um clan.")
                .color(ChatColor.RED).create())
        }

        if (clan.members.size >= clan.maxMembers) {
            return sender.message(ComponentBuilder("O clan atingiu o limite de membros.")
                .color(ChatColor.RED).create())
        }

        if (!target.account.preferences.privacy.receiveClanInvitations) {
            return sender.message(ComponentBuilder(
                "§aEste jogador desativou os pedidos de clan."
            ).color(ChatColor.RED).create())
        }

        if (clanManager.hasPendingInvite(targetName, clan)) {
            return sender.message(ComponentBuilder("Você já convidou este jogador recentemente.")
                .color(ChatColor.RED).create())
        }

        target.sendMessage(*ComponentBuilder("§b§lCLAN §8» ")
            .append("Você foi convidado para o clan ${clan.name} (${clan.tag})").color(ChatColor.YELLOW)
            .append("\n§b§lCLAN §8» ")
            .append(TextComponent("§a§lCLIQUE AQUI §epara aceitar.").apply {
                clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan aceitar ${clan.tag}")
            }).color(ChatColor.YELLOW)
            .create())

        clanManager.createInvite(targetName, clan, 1, TimeUnit.MINUTES)

        clan.broadcast(ComponentBuilder("§b§lCLAN §8» ")
            .append("${sender.name} convidou $targetName para o clan.").color(ChatColor.YELLOW)
            .create())
    }
}