package club.helix.bungeecord.command

import club.helix.bungeecord.kotlin.player.account
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import club.helix.components.account.HelixRank
import club.helix.components.clan.Clan
import club.helix.components.clan.ClanManager
import club.helix.components.clan.ClanRole
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import java.util.concurrent.TimeUnit

class ClanCreateCMD(
    private val clanManager: ClanManager,
    private val sender: BungeeCommandSender,
    private val args: Array<String>
) {
    fun execute() {
        clanManager.getClanByMember(sender.name)?.also {
            return sender.message(ComponentBuilder("Você já está em um clan.")
                .color(ChatColor.RED).create())
        }
        val rank = sender.player.account.mainRankLife.rank
        println(args.joinToString(", "))

        if (!HelixRank.vip(rank) && TimeUnit.MILLISECONDS.toDays(sender.player.account.onlineTime) < 7) {
            return sender.message(ComponentBuilder("§cVocê precisa de 7 dias de jogo em nossa rede ou " +
                    "precisa ter um plano vip ativado para criar um clan.").color(ChatColor.RED).create())
        }

        if (args.size < 3) {
            return sender.message(ComponentBuilder("§cUtilize /clan criar <tag> <nome> para criar um clan.")
                .color(ChatColor.RED).create())
        }

        val tag = args[1].uppercase().apply {
            if (clanManager.clanPattern.matcher(this).find()) {
                return sender.message(ComponentBuilder("A tag do clan contém caractéres inválidos.").color(ChatColor.RED)
                    .append("Você só pode utilizar letras e números.").color(ChatColor.RED)
                    .create())
            }
            if (this.length < 3 || this.length > 6) {
                return sender.message(ComponentBuilder("A tag do clan precisa conter entre 3 e 6 caractéres.")
                    .color(ChatColor.RED).create())
            }
            clanManager.getClanByTag(this)?.also {
                return sender.message(ComponentBuilder("Já existe um clan com esta tag.")
                    .color(ChatColor.RED).create())
            }
        }

        val nameBuilder = StringBuilder()
        for (i in 2 until args.size) {
            nameBuilder.append(args[i]).append(" ")
        }

        val name = nameBuilder.toString().trim().apply {
            if (clanManager.clanPattern.matcher(this).find()) {
                return sender.message(ComponentBuilder("O nome do clan contém caractéres inválidos.\n").color(ChatColor.RED)
                    .append("Você só pode utilizar letras e números.").color(ChatColor.RED).create())
            }
            if (this.length < 4 || this.length > 15) {
                return sender.message(ComponentBuilder("O nome do clan precisa conter entre 4 e 15 caractéres.")
                    .color(ChatColor.RED).create())
            }
            clanManager.getClanByName(this)?.also {
                return sender.message(ComponentBuilder("Já existe um clan com este nome.")
                    .color(ChatColor.RED).create())
            }
        }

        val clan = Clan(name, tag).apply {
            addMember(sender.name, ClanRole.LEADER); recalculate(rank)
            clanManager.register(this); clanManager.controller.save(this)
        }.apply { recalculate(rank) }

        sender.message(ComponentBuilder(" §a§lYAY §aSeu clan foi criado:\n").color(ChatColor.GREEN)
            .append("   Nome: ${clan.name}\n").color(ChatColor.GREEN)
            .append("   Tag: ${clan.tag}\n").color(ChatColor.GREEN)
            .append("   Limite de membros: ${clan.maxMembers}\n").color(ChatColor.GREEN)
            .append("   Limite de gerentes: ${clan.maxManagers}\n").color(ChatColor.GREEN)
            .append("Gerencie seu clan utilizando /clan.\n").color(ChatColor.GREEN)
            .create()
        )
    }
}