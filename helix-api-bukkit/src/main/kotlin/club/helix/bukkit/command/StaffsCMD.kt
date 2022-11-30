package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.account.HelixRank
import club.helix.components.command.executor.CommandOptions
import club.helix.components.server.HelixServer

class StaffsCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    @CommandOptions(
        name = "staffs",
        permission = true,
        description = "Ver staffs online."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val staffs = HelixServer.networkPlayers.mapNotNull {
            apiBukkit.components.userManager.redisController.load(it)
        }.filter { HelixRank.staff(it.mainRankLife.rank) }

        if (staffs.isEmpty()) {
            return sender.sendMessage("§cNão há staffs online.")
        }

        sender.sendMessage("§aMembros da equipe online:")
        staffs.map { it.mainRankLife.rank }.sortedBy { it.ordinal }.distinct().forEach { rank ->
            sender.sendMessage("${rank.color}${rank.displayName}:")

            staffs.filter { it.mainRankLife.rank == rank }.forEach {
                sender.sendMessage("§8 - §7${it.name} §e(${HelixServer.getPlayerServer(it.name)?.displayName ?: "§cerror-404"})")
            }
        }
    }
}