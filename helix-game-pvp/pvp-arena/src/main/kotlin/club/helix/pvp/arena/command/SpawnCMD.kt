package club.helix.pvp.arena.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.pvp.arena.PvPArena

class SpawnCMD(private val plugin: PvPArena): BukkitCommandExecutor() {

    @CommandOptions(
        name = "spawn",
        target = CommandTarget.PLAYER,
        description = "Ir ao spawn."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        plugin.serverSpawnHandle.send(sender.player)
    }
}