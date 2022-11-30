package club.helix.bungeecord.command

import club.helix.components.clan.ClanManager
import club.helix.components.clan.ClanRole
import club.helix.components.clan.exception.ClanMemberNotFoundException
import club.helix.components.util.HelixTimeData
import club.helix.bungeecord.kotlin.clan.broadcast
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import java.util.concurrent.TimeUnit

class ClanLeaveCMD(
    private val clanManager: ClanManager,
    private val sender: BungeeCommandSender
) {
    fun execute() {
        val clan = clanManager.getClanByMember(sender.name)
            ?: return sender.message(ComponentBuilder("Você não possui um clan.")
                .color(ChatColor.RED).create())
        val member = clan.getMember(sender.name)
            ?: return sender.message(ComponentBuilder("Ocorreu um erro ao carregar sua patente.")
                .color(ChatColor.RED).create())

        if (member.role == ClanRole.LEADER) {
            return sender.message(ComponentBuilder(" líder do clan não pode sair do clan.\n").color(ChatColor.RED)
                .append("Utilize /clan deletar caso queira deletar.").color(ChatColor.RED)
                .create())
        }

        if (HelixTimeData.isActive(sender.name, "clan-leave")) {
            try {
                clan.removeMember(member)
                clanManager.controller.save(clan)
            }catch (error: ClanMemberNotFoundException) {
                return sender.message(ComponentBuilder("Ocorreu um erro ao sair do clan. (ClanMemberNotFound)")
                    .color(ChatColor.RED).create())
            }catch(error: Exception) {
                error.printStackTrace()
                return sender.message(ComponentBuilder("Ocorreu um erro ao sair do clan. (Internal Error)")
                    .color(ChatColor.RED).create())
            }
            sender.message(ComponentBuilder("Você saiu do clan ${clan.name}.")
                .color(ChatColor.RED).create())
            return clan.broadcast(ComponentBuilder("§b§lCLAN §7» ")
                .append("${sender.name} saiu do clan!").color(ChatColor.RED)
                .create())
        }

        HelixTimeData.putTime(sender.name, "clan-leave", 6, TimeUnit.SECONDS)

        sender.message(ComponentBuilder("§d§lCLAN §8» ")
            .append("Você tem certeza que deseja sair do clan ${clan.name}?").color(ChatColor.YELLOW)
            .append("\n§d§lCLAN §8» ")
            .append("Execute o comando novamente para confirmar.").color(ChatColor.YELLOW)
            .create())
    }
}