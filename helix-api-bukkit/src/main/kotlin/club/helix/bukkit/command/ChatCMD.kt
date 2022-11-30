package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.account.HelixRank
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit

class ChatCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    private fun help(sender: BukkitCommandSender) = sender.sendMessage(arrayOf(
        "§a/chat on - Ativar o bate-papo.",
        "§a/chat off - Desativar o bate-papo.",
        "§a/chat clear - Limpar o bate-papo.",
        "§a/chat status - Status do bate-papo."
    ))

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, mutableListOf("on", "off", "clear", "status"))
    )

    @CommandOptions(
        name = "chat",
        permission = true,
        description = "Gerenciar o chat."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty()) return help(sender)
        val chat = apiBukkit.serverChat

        when(args[0].lowercase()) {
            "on", "off" -> {
                val toggle = args[0].lowercase() == "on"

                if (chat.active == toggle) {
                    return sender.sendMessage(if (toggle) "§cO chat já está ativado." else "§cO chat já foi desativado por ${chat.deactivatedBy ?: "DESCONHECIDO"}.")
                }
                chat.active = toggle
                chat.deactivatedBy = if (!toggle) sender.name else null

                Bukkit.getOnlinePlayers().forEach {
                    it.spigot().sendMessage(TextComponent(
                        if (toggle) "§aO bate-papo foi ativado." else "§cO bate-papo foi desativado."
                    ).apply {
                        val rank = it.account.mainRankLife.rank
                        if (rank.isBiggerThen(HelixRank.AJUDANTE)) {
                            hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder(
                                "§7${if (toggle) "Ativado" else "Desativado"} por: ${rank.color}${sender.name}"
                            ).create())
                        }
                    })
                }
            }
            "clear", "limpar" -> {
                chat.clear()
                Bukkit.getOnlinePlayers().forEach {
                    it.spigot().sendMessage(TextComponent("§aO bate-papo foi limpo!").apply {
                        val rank = it.account.mainRankLife.rank
                        if (rank.isBiggerThen(HelixRank.AJUDANTE)) {
                            hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder(
                                "§7Limpo por: ${rank.color}${sender.name}"
                            ).create())
                        }
                    })
                }
            }
            "status", "info" -> {
                sender.sendMessage(if (chat.active)
                    "§6O chat está ativado." else
                        "§6Estado: §cDesativado\n§6Responsável: §f${chat.deactivatedBy ?: "DESCONHECIDO"}")
            }
            else -> help(sender)
        }
    }
}