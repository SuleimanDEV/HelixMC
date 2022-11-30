package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.util.HelixAddress
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor

class HelpCMD: BukkitCommandExecutor() {

    @CommandOptions(
        name = "help",
        target = CommandTarget.PLAYER,
        description = "Obter ajuda.",
        aliases = ["ajuda"]
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        sender.sendMessage("§aCategorias disponíveis:\n")

        sender.player.spigot().sendMessage(
            TextComponent("§8- §7"),
            TextComponent("${ChatColor.GRAY}Comunidade do servidor\n").apply {
                hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("§7Clique para selecionar.").create())
                clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://${HelixAddress.DISCORD}")
            },
            TextComponent("§8- §7"),
            TextComponent("${ChatColor.GRAY}Relatar um erro ou problema\n").apply {
                hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("§7Clique para selecionar.").create())
                clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://${HelixAddress.DISCORD}")
            },
            TextComponent("§r  §f* "),
            TextComponent("${ChatColor.GRAY}Denunciar infratores\n").apply {
                hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("§7Clique para selecionar.").create())
                clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/reportar <jogador> <motivo>")
            },
            TextComponent("§r  §f* "),
            TextComponent("${ChatColor.GRAY}Loja do servidor\n").apply {
                hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("§7Clique para selecionar.").create())
                clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://${HelixAddress.SHOP}")
            },
            TextComponent("§8- §7"),
            TextComponent("${ChatColor.GRAY}Regras de conduta\n").apply {
                hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("§7Clique para selecionar.").create())
                clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://${HelixAddress.SHOP}/regras")
            }
        )
        sender.sendMessage("§ePrecisa de mais ajuda ? §bAbra um ticket em nossa comunidade.")
    }
}