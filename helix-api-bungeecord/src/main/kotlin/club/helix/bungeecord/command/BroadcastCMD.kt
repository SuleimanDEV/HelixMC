package club.helix.bungeecord.command

import club.helix.bungeecord.kotlin.player.account
import club.helix.bungeecord.precommand.executor.BungeeCommandExecutor
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import club.helix.components.account.HelixRank
import club.helix.components.command.executor.CommandOptions
import club.helix.components.server.HelixServer
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent

class BroadcastCMD: BungeeCommandExecutor() {

    @CommandOptions(
        name = "broadcast",
        permission = true,
        description = "Enviar mensagem para todos.",
        aliases = ["bc"]
    )
    override fun execute(sender: BungeeCommandSender, args: Array<String>) {
        sender.takeIf {
            it.isPlayer && HelixServer.getPlayerServer(it.name)?.let { server -> server.type == HelixServer.LOGIN} == true
        }?.apply { return }

        if (args.isEmpty()) {
            return sender.message(
                ComponentBuilder("Utilize /broadcast <mensagem> para fazer um anúncio")
                    .color(ChatColor.RED).create()
            )
        }
        val message = ChatColor.translateAlternateColorCodes('&', args.joinToString(" "))

        ProxyServer.getInstance().players.filter {
            HelixServer.getServer(it.server.info.name)?.type != HelixServer.LOGIN
        }.forEach {

            it.sendMessage(TextComponent("§d§lHELIX §7»").apply {
                if (HelixRank.staff(it.account.mainRankLife.rank)) {
                    hoverEvent = HoverEvent(
                        HoverEvent.Action.SHOW_TEXT, ComponentBuilder(
                        "§7Enviado por: §f${if (sender.isPlayer) "${sender.player.account.mainRankLife.rank.color}${sender.name}" else "§4CONSOLE"}"
                    ).create())
                }
            },
            TextComponent(" "),
            *ComponentBuilder("§f$message").create(),
            )
        }
    }
}