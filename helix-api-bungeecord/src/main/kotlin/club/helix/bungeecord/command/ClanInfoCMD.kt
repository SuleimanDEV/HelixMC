package club.helix.bungeecord.command

import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import club.helix.components.clan.Clan
import club.helix.components.clan.ClanManager
import club.helix.components.clan.ClanRole
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import club.helix.components.util.HelixTimeData
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class ClanInfoCMD(
    private val clanManager: ClanManager,
    private val sender: BungeeCommandSender,
    private val args: Array<String>
) {
    fun execute() {
        if (HelixTimeData.getOrCreate(sender.name, "clan-info-cmd", 3, TimeUnit.SECONDS)) {
            return sender.message(ComponentBuilder(
                "§cAguarde ${HelixTimeData.getTimeFormatted(sender.name, "clan-info-cmd")} para executar este comando novamente."
            ).color(ChatColor.RED).create())
        }

        if (args.size == 1) {
            val clan = clanManager.getClanByMember(sender.name)
                ?: return sender.message(ComponentBuilder("Utilize /clan info <tag/nome>.")
                    .color(ChatColor.RED).create())
             return sender.message(info(clan))
        }

        val clanId = args[1]
        val clan = (clanManager.getClanByTag(clanId)
            ?: clanManager.getClanByName(clanId))
            ?: clanManager.getClanByMember(clanId)
            ?: clanManager.controller.loadByTag(clanId)
            ?: clanManager.controller.loadByName(clanId)
            ?: clanManager.controller.loadByMember(clanId)
            ?: return sender.message(ComponentBuilder("Clan não encontrado")
                .color(ChatColor.RED).create())

        sender.message(info(clan))
    }

    private fun info(clan: Clan) = ComponentBuilder("§aInformações de ${clan.tag}:\n")
        .append("§7Nome: §e${clan.name} (${clan.tag})\n")
        .append("§7Líder: ${clan.leader.run { "${role.color}$name" }}\n")
        .append("§7Experiência: §3${clan.exp.decimalFormat()}\n")
        .append("§7Gerentes: ${ClanRole.MANAGER.color}${clan.managers.size}/${clan.maxManagers}\n")
        .append("§7Criado em: §a${SimpleDateFormat("dd/MM/yyyy HH:mm").format(clan.createdAt)}\n")
        .append("\n")
        .append("§7Membros §8(${clan.members.size}/${clan.maxMembers})§7:\n")
        .append(clan.members.joinToString("§7, ") { "${it.role.color}${it.name}" })
        .create()
}