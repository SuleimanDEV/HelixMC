package club.helix.bukkit.command

import club.helix.bukkit.kotlin.player.Build.Companion.build
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import org.bukkit.Bukkit

class BuildCMD: BukkitCommandExecutor() {

    @CommandOptions(
        name = "build",
        permission = true,
        description = "Ativar/Desativar modo construção."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (sender.isConsole && args.isEmpty()) {
            return sender.sendMessage("§cUtilize /build <jogador> para ativar/desativar o modo construção.")
        }

        val targetName = if (sender.isPlayer && args.isEmpty()) sender.name else args[0]
        val target = Bukkit.getPlayer(targetName)
            ?: return sender.sendMessage("§cJogador offline.")
        val build = target.build.apply { target.build(!this) }

        if (target.name == sender.name) {
            sender.sendMessage(if (build) "§eAgora você não pode construir." else "§aAgora você pode construir.")
        }else {
            sender.sendMessage(if (build) "§e${target.name} agora não pode construir." else
                "§a${target.name} agora pode construir.")
        }
    }
}