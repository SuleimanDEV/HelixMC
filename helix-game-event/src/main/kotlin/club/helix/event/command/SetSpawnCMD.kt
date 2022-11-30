package club.helix.event.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.event.EventPlugin

class SetSpawnCMD(private val plugin: EventPlugin): BukkitCommandExecutor() {

    @CommandOptions(
        name = "setspawn",
        target = CommandTarget.PLAYER,
        permission = true,
        description = "Alterar localização do spawn.",
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        plugin.spawnLocation = sender.player.location
        sender.sendMessage("§aSpawn alterado para sua localização atual.")
    }
}