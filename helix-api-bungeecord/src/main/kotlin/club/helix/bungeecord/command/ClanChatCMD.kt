package club.helix.bungeecord.command

import club.helix.bungeecord.HelixBungee
import club.helix.bungeecord.kotlin.player.account
import club.helix.bungeecord.precommand.executor.BungeeCommandExecutor
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.server.HelixServer
import club.helix.components.util.CensureKeyWords
import club.helix.components.util.HelixTimeData
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import java.util.concurrent.TimeUnit

class ClanChatCMD(private val plugin: HelixBungee): BungeeCommandExecutor() {

    @CommandOptions(
        name = "cc",
        target = CommandTarget.PLAYER,
        description = "Enviar mensagem para membros do clan."
    )
    override fun execute(sender: BungeeCommandSender, args: Array<String>) {
        val clan = plugin.components.clanManager.getClanByMember(sender.name)
            ?: return sender.message(ComponentBuilder("Você não possui um clan.")
                .color(ChatColor.RED).create())

        if (args.isEmpty()) {
            return sender.message(ComponentBuilder("Utilize /cc <mensagem> para enviar mensagem para os membros do clan.")
                .color(ChatColor.YELLOW).create())
        }

        val member = clan.getMember(sender.name)
            ?: return sender.message(ComponentBuilder("Ocorreu um erro ao carregar sua patente.")
                .color(ChatColor.RED).create())
        val message = CensureKeyWords.matcher(args.joinToString(" "))

        if (HelixTimeData.getOrCreate(sender.name, "clan-chat-cmd", 3, TimeUnit.SECONDS)) {
            return sender.message(ComponentBuilder(
                "Aguarde ${HelixTimeData.getTimeFormatted(sender.name, "clan-chat-cmd")} para executar este comando novamente."
            ).color(ChatColor.RED).create())
        }

        try {
            plugin.proxy.players.filter {
                clan.containsMember(it.name) &&
                        HelixServer.getPlayerServer(it.name)?.let {
                                server -> server.type != HelixServer.LOGIN
                        } == true
                        && it.account.preferences.chat.receiveClanMessage
            }.forEach {
                it.sendMessage(*ComponentBuilder("§b§lCLAN §8» ")
                    .append("${member.role.color}${sender.name}: ")
                    .append(message).color(ChatColor.WHITE).create())
            }
        }catch (error: Exception) {
            error.printStackTrace()
            sender.message(ComponentBuilder("Ocorreu um erro ao enviar mensagem para os membros do clan.")
                .color(ChatColor.RED).create())
        }
    }
}