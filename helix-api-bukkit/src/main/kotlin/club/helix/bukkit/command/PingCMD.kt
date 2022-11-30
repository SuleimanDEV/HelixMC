package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PingCMD: BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, Bukkit.getOnlinePlayers().map(Player::getName).toMutableList())
    )

    private fun ping(player: Player): Int = player.let {
        val handle = it.javaClass.getMethod("getHandle").invoke(it)
        handle.javaClass.getDeclaredField("ping").get(handle) as Int
    }

    @CommandOptions(
        name = "ping",
        description = "Verificar latência dos jogadores."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty() && sender.isConsole) {
            return sender.sendMessage("§eUtilize §f/ping <jogador> para verificar a latência de um jogador.")
        }

        val target = Bukkit.getPlayer(if (args.isNotEmpty()) args.first() else sender.name)
            ?: return sender.sendMessage("§cJogador offline.")
        val ping = ping(target)

        sender.sendMessage(if (sender.name == target.name) "§7Seu ping: §f$ping ms" else
            "§7Ping de ${target.name}: §f$ping ms")
    }
}