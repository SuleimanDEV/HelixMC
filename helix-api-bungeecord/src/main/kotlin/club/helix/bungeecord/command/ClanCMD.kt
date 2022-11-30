package club.helix.bungeecord.command

import club.helix.bungeecord.HelixBungee
import club.helix.bungeecord.precommand.executor.BungeeCommandExecutor
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.connection.ProxiedPlayer

class ClanCMD(private val plugin: HelixBungee): BungeeCommandExecutor() {

    override fun onTabComplete(sender: BungeeCommandSender) = mutableListOf(
        CommandCompleter(0, mutableListOf("criar", "convidar", "deletar", "info", "sair",
            "promover", "rebaixar", "expulsar")),
        CommandCompleter(1, plugin.proxy.players.map(ProxiedPlayer::getName).toMutableList())
    )

    @CommandOptions(
        name = "clan",
        target = CommandTarget.PLAYER,
        description = "Gerenciar clans."
    )
    override fun execute(sender: BungeeCommandSender, args: Array<String>) {
        if (args.isEmpty()) return help(sender)
        val clanManager = plugin.components.clanManager


        if (args.size == 1) {
            return when (args[0].lowercase()) {
                "criar", "create" -> ClanCreateCMD(clanManager, sender, args).execute()
                "convidar", "invite" -> ClanInviteCMD(clanManager, sender, args).execute()
                "deletar", "delete", "del" -> ClanDeleteCMD(clanManager, sender).execute()
                "info", "ver" -> ClanInfoCMD(clanManager, sender, args).execute()
                "sair", "leave" -> ClanLeaveCMD(clanManager, sender).execute()
                "promover", "promote" -> ClanPromoteCMD(clanManager, sender, args).execute()
                "rebaixar", "demote" -> ClanDemoteCMD(clanManager, sender, args).execute()
                "expulsar", "kick" -> ClanKickCMD(clanManager, sender, args).execute()
                else -> help(sender)
            }
        }

        if (args.size == 2) return when (args[0].lowercase()) {
            "criar", "create" -> ClanCreateCMD(clanManager, sender, args).execute()
            "convidar", "invite" -> ClanInviteCMD(clanManager, sender, args).execute()
            "aceitar", "accept" -> ClanAcceptCMD(clanManager, sender, args).execute()
            "info", "ver" -> ClanInfoCMD(clanManager, sender, args).execute()
            "promover", "promote" -> ClanPromoteCMD(clanManager, sender, args).execute()
            "rebaixar", "demote" -> ClanDemoteCMD(clanManager, sender, args).execute()
            "expulsar", "kick" -> ClanKickCMD(clanManager, sender, args).execute()
            else -> help(sender)
        }

        if (args.size >= 3) return when (args[0].lowercase()) {
            "criar", "create" -> ClanCreateCMD(clanManager, sender, args).execute()
            else -> help(sender)
        }

    }

    private fun help(sender: BungeeCommandSender) = sender.message(
        ComponentBuilder("Comandos do clan:").color(ChatColor.GREEN)
            .append("\n/clan criar <tag> <nome>").color(ChatColor.GREEN)
            .append(" - Criar clan.").color(ChatColor.GRAY)
            .append("\n/clan convidar <jogador>").color(ChatColor.GREEN)
            .append(" - Convidar jogador.").color(ChatColor.GRAY)
            .append("\n/clan promover <membro>").color(ChatColor.GREEN)
            .append(" - Promover membro para Gerente.").color(ChatColor.GRAY)
            .append("\n/clan rebaixar <membro>").color(ChatColor.GREEN)
            .append(" - Rebaixar membro.").color(ChatColor.GRAY)
            .append("\n/clan expulsar <membro>").color(ChatColor.GREEN)
            .append("- Expulsar membro.").color(ChatColor.GRAY)
            .append("\n/clan deletar").color(ChatColor.GREEN)
            .append(" - Deletar clan.").color(ChatColor.GRAY)
            .append("\n/clan sair").color(ChatColor.GREEN)
            .append(" - Sair do clan.").color(ChatColor.GRAY)
            .append("\n/clan info [nome/tag]").color(ChatColor.GREEN)
            .append(" - Ver informações do clan.").color(ChatColor.GRAY)
            .append(" - Enviar mensagem para membros.").color(ChatColor.GRAY)
            .create()
    )
}