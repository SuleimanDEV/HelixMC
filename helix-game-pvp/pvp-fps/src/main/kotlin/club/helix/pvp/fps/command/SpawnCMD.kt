package club.helix.pvp.fps.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.pvp.fps.PvPFps

class SpawnCMD(private val plugin: PvPFps): BukkitCommandExecutor() {

    @CommandOptions(
        name = "spawn",
        target = CommandTarget.PLAYER,
        description = "Ir para o spawn."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        plugin.serverSpawnHandle.send(sender.player)
    }
}