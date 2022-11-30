package club.helix.pvp.arena.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.pvp.arena.PvPArena
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent

class FeastCMD(private val plugin: PvPArena): BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, mutableListOf("compass"))
    )

    @CommandOptions(
        name = "feast",
        description = "Visualizar informações do feast."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val feast = plugin.feastRunnable

        if (args.isNotEmpty() && sender.isPlayer && args[0].lowercase() == "compass") {
            sender.isPlayer
            sender.player.compassTarget = feast.center
            return sender.sendMessage("§6Localizador apontando para o feast.")
        }

        val minutes = feast.time / 60
        val seconds = feast.time % 60

        val spawnTime = StringBuilder().apply {
            if (minutes > 0) {
                append(minutes).append("m").append(" ")
            }
            append(seconds).append("s")
        }

        sender.sendMessage(if (feast.spawned) "§aO feast já nasceu!" else
            "§cO feast irá nascer em ${spawnTime.toString().trim()}.")

        if (sender.isPlayer) {
            sender.player.spigot().sendMessage(TextComponent(
                "(Apontar localizador para o feast)"
            ).apply {
                color = ChatColor.YELLOW
                clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/feast compass")
                hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("§7/feast compass").create())
            })
        }
    }
}