package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent

class DevLocationCMD: BukkitCommandExecutor() {

    @CommandOptions(
        name = "devloc",
        target = CommandTarget.PLAYER,
        permission = true,
        description = "Visualizar localização atual."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val location = sender.player.location

        sender.player.spigot().sendMessage(
            TextComponent("§aLocalização atual: §f${location.x}, ${location.y}, ${location.z}"),
            TextComponent(" "),
            TextComponent("§7[COPIAR]").apply {
                clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "${location.x}, ${location.y}, ${location.z}, ${location.yaw}f, ${location.pitch}f")
            }
        )
    }
}