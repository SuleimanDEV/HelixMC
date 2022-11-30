package club.helix.pvp.lava.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.pvp.lava.PvPLava

class SpawnCMD(private val plugin: PvPLava): BukkitCommandExecutor() {

    @CommandOptions(
        name = "spawn",
        target = CommandTarget.PLAYER,
        description = "Ir para o spawn."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>): Unit =
        run { plugin.serverSpawn.send(sender.player) }
}