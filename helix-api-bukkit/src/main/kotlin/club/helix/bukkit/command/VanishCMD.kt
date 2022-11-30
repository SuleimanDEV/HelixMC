package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.bukkit.util.VanishPlayers
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget

class VanishCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    @CommandOptions(
        name = "vanish",
        target = CommandTarget.PLAYER,
        permission = true,
        description = "Ficar invisível.",
        aliases = ["v"]
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>): Unit = sender.player.account.run {
        vanish = !vanish
        apiBukkit.components.userManager.redisController.save(this)

        VanishPlayers.handleVanish(sender.player)
        sender.sendMessage(if (vanish) "§aVocê está invisível para jogadores e staffs inferiores." else
            "§dVocê está visível novamente.")
    }
}