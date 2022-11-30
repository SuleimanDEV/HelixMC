package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.account.HelixRank
import club.helix.components.command.executor.CommandOptions
import club.helix.components.server.HelixServer

class PartnersCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    @CommandOptions(
        name = "parceiros",
        description = "Mostrar parceiros disponíveis."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (!sender.isConsole && sender.player.account.mainRankLife.rank.isLessThen(HelixRank.YOUTUBER)) {
            return sender.sendMessage("§cVocê não tem permissão para utilizar este comando.")
        }

        val partners = HelixServer.networkPlayers.mapNotNull {
            apiBukkit.components.userManager.getUser(it)
                ?: apiBukkit.components.userManager.redisController.load(it)
        }.filter {
            it.mainRankLife.rank == HelixRank.YOUTUBER || it.mainRankLife.rank == HelixRank.YOUTUBER_PLUS
        }

        if (partners.isEmpty()) {
            return sender.sendMessage("§cNão há parceiros online.")
        }

        sender.sendMessage("§3Parceiros online (${partners.size}):")
        partners.forEach {
            sender.sendMessage(" §8- ${it.mainRankLife.rank.color}${it.name} §e(${HelixServer.getPlayerServer(it.name)?.displayName ?: "§c{error-404}"})")
        }
    }
}