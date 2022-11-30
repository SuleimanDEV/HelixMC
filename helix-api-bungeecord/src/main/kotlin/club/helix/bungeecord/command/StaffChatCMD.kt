package club.helix.bungeecord.command

import club.helix.bungeecord.kotlin.player.account
import club.helix.bungeecord.precommand.executor.BungeeCommandExecutor
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.server.HelixServer
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent

class StaffChatCMD: BungeeCommandExecutor() {

    @CommandOptions(
        name = "staffchat",
        description = "Comunicar-se com os membros da equipe.",
        target = CommandTarget.PLAYER,
        aliases = ["s", "sc"],
        permission = true
    )
    override fun execute(sender: BungeeCommandSender, args: Array<String>) {
        if (!sender.player.account.preferences.staff.receivedStaffMessage) {
            return sender.message(ComponentBuilder("Ative as mensagens da staff para poder enviar mensagens.")
                .color(ChatColor.RED).create())
        }

        if (args.isEmpty()) {
            return sender.message(ComponentBuilder("Utilize §f/s <mensagem> §6para comunicar-se com os membros da equipe.")
                .color(ChatColor.GOLD).create())
        }

        try {
            val rank = sender.player.account.mainRankLife.rank
            val currentServer = HelixServer.getServer(sender.player.server.info.name)
                ?: return sender.message(ComponentBuilder("Não foi possível encontrar seu servidor.")
                    .color(ChatColor.RED).create())

            val message = args.joinToString(" ")
            val prefix = "§8§l[SC]"

            ProxyServer.getInstance().players.filter {
                it.hasPermission("helix.cmd.staffchat") && it.account.preferences.staff.receivedStaffMessage
                        && currentServer.type != HelixServer.LOGIN
            }.forEach {
                it.sendMessage(
                    TextComponent(prefix), TextComponent(" "),
                    TextComponent("${rank.prefix}${sender.name}:").apply {
                        hoverEvent = HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, ComponentBuilder(
                            "§7Servidor: §f${currentServer.displayName} (${currentServer.serverName})"
                        ).create())
                        clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell ${sender.name} ")
                    },
                    TextComponent(" "),
                    TextComponent(message)
                )
            }
        }catch (error: Exception) {
            error.printStackTrace()
            sender.message(ComponentBuilder("Ocorreu um erro ao enviar mensagem no chat da equipe. Contate um superior.")
                .color(ChatColor.RED).create())
        }
    }
}