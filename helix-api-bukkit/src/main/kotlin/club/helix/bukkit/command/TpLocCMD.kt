package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import org.bukkit.Location

class TpLocCMD: BukkitCommandExecutor() {

    @CommandOptions(
        name = "tploc",
        permission = true,
        target = CommandTarget.PLAYER,
        description = "Teleportar-se para uma localização."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.size < 3) {
            return sender.sendMessage("§cUtilize /tploc <x> <y> <z>")
        }

        val x = args[0].toDoubleOrNull() ?: return sender.sendMessage("§cA posição x é inválida.")
        val y = args[1].toDoubleOrNull() ?: return sender.sendMessage("§cA posição y é inválida.")
        val z = args[2].toDoubleOrNull() ?: return sender.sendMessage("§cA posição z é inválida.")

        sender.player.teleport(Location(sender.player.world, x, y, z))
        sender.sendMessage("§7Enviado para: §f$x, $y, $z")
    }
}