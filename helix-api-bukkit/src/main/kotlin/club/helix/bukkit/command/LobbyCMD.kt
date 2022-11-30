package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.server.HelixServer
import club.helix.components.util.HelixTimeData
import java.util.concurrent.TimeUnit

class LobbyCMD(private val plugin: HelixBukkit): BukkitCommandExecutor() {

    @CommandOptions(
        name = "lobby",
        target = CommandTarget.PLAYER,
        description = "Ir para o lobby.",
        aliases = ["hub", "l"]
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) = HelixBukkit.instance.run {
        val availableLobby = plugin.currentServer.findAvailableLobby()
            ?: return@run sender.sendMessage("§cNão há um lobby disponível.")

        plugin.currentServer.takeIf { it.type == availableLobby.type && it.category == HelixServer.Category.LOBBY }?.run {
            return sender.sendMessage("§eVocê já está no lobby.")
        }

        if (sender.player.account.preferences.lobby.gameProtection && HelixBukkit.instance.currentServer.category != HelixServer.Category.LOBBY &&
            !HelixTimeData.isActive(sender.name, "lobby-command")) {
            HelixTimeData.putTime(sender.name, "lobby-command", 3, TimeUnit.SECONDS)
            return sender.sendMessage("§eVocê tem certeza? Utilize §f/lobby §enovamente para voltar ao lobby.")
        }
        sender.player.connect(availableLobby, true)
    }
}