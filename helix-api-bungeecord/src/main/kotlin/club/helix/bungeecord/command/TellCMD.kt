package club.helix.bungeecord.command

import club.helix.bungeecord.HelixBungee
import club.helix.bungeecord.kotlin.player.account
import club.helix.bungeecord.precommand.executor.BungeeCommandExecutor
import club.helix.bungeecord.precommand.sender.BungeeCommandSender
import club.helix.components.account.HelixRank
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.server.HelixServer
import club.helix.components.util.CensureKeyWords
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent

class TellCMD(private val plugin: HelixBungee): BungeeCommandExecutor() {

    override fun onTabComplete(sender: BungeeCommandSender) = mutableListOf(
        CommandCompleter(0, HelixServer.networkPlayers)
    )

    @CommandOptions(
        name = "tell",
        description = "Enviar mensagem para jogadores.",
        aliases = ["message", "msg"]
    )
    override fun execute(sender: BungeeCommandSender, args: Array<String>) {
        val userManager = plugin.components.userManager

        if (sender.isConsole && args.size < 2 || sender.isPlayer && args.isEmpty()) {
            return sender.sendMessage(*ComponentBuilder(
                "Utilize /tell <jogador> <mensagem> para enviar uma mensagem."
            ).color(ChatColor.RED).create())
        }

        if (args.size == 1 && (args[0].lowercase() == "on" || args[0].lowercase() == "off")) {
            val toggle = args[0].lowercase() =="on"

            sender.player.account.apply {
                if (preferences.privacy.receivePrivateMessage == toggle) {
                    return sender.sendMessage(*ComponentBuilder(
                        "Seu recebimento de mensagens privadas já está ${if (toggle) "ativado" else "desativado"}."
                    ).color(ChatColor.RED).create())
                }

                preferences.privacy.receivePrivateMessage = toggle
                userManager.saveAll(this, false)
                return@apply sender.sendMessage(*ComponentBuilder(
                    if (toggle) "Você ativou o recebimento de mensagens privadas."  else
                        "Você desativou o recebimento de mensagens privadas."
                ).color(if (toggle) ChatColor.GREEN else ChatColor.RED).create())
            }
        }

        if (args[0].lowercase() == sender.name.lowercase()) {
            return sender.sendMessage(*ComponentBuilder(
                "Você não pode enviar mensagem para sí mesmo."
            ).create())
        }

        val targetName = args[0]
        val targetPlayer = plugin.proxy.getPlayer(targetName)
            ?: return sender.sendMessage("§cJogador offline.")
        val targetAccount = targetPlayer.account

        if (!targetAccount.preferences.privacy.receivePrivateMessage && sender.isPlayer
            && !HelixRank.staff(sender.player.account.mainRankLife.rank)) {
            return sender.sendMessage(*ComponentBuilder(
                "Este jogador está com o recebimento de mensagens privadas desativado."
            ).color(ChatColor.RED).create())
        }

        try {
            val senderDisplayName = if (sender.isPlayer) sender.player.account.run {
                "${tag.color}${name}"
            } else "§4CONSOLE"

            val messageBuilder = StringBuilder()
            for (i in 1 until args.size) {
                messageBuilder.append(args[i]).append(" ")
            }
            val message = CensureKeyWords.matcher(messageBuilder.toString().trim())

            targetPlayer.sendMessage(
                TextComponent("§aMensagem de $senderDisplayName§a:").apply {
                    clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell ${sender.name} ")
                    hoverEvent = HoverEvent(
                        HoverEvent.Action.SHOW_TEXT, ComponentBuilder("§7Clique para responder.").create())
                    },
                TextComponent(" "),
                TextComponent(message)
            )

            sender.sendMessage("§aMensagem para ${targetAccount.tag.color}${targetPlayer.name}§a: §f$message")
        }catch (error: Exception) {
            error.printStackTrace()
            sender.sendMessage("§cOcorreu um erro ao enviar esta mensagem.")
        }
    }
}