package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.HelixComponents
import club.helix.components.account.HelixRank
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.server.HelixServer
import com.google.gson.JsonObject

class RemoveRankCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf<CommandCompleter>().apply {
        add(CommandCompleter(0, HelixServer.networkPlayers))

        val ranks = HelixRank.values().filter { sender.hasPermission("helix.cmd.${it.toString().lowercase()}") }
        add(CommandCompleter(1, ranks.map { it.displayName }.toMutableList()))
    }

    @CommandOptions(
        name = "removerank",
        description = "Remover rank de um jogador.",
        permission = true
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.size < 2) {
            return sender.sendMessage("§cUtilize /removerank <jogador> <rank> para remover um rank.")
        }

        val targetName = args[0]
        val userManager = apiBukkit.components.userManager
        val target = userManager.getUser(targetName)
            ?: userManager.redisController.load(targetName)
            ?: userManager.userSqlController.load(targetName)
            ?: return sender.sendMessage("§cJogador não registrado.")

        val availableRanks = HelixRank.values().filter {
            sender.hasPermission("helix.rank.${it.toString().lowercase()}")
        }
        val rank = HelixRank.get(args[1]) ?: return sender.sendMessage("§cRank não encontrado.")

        if (rank == HelixRank.DEFAULT) {
            return sender.sendMessage("§cVocê não pode remover o rank padrão.")
        }

        if (!availableRanks.contains(rank)) {
            return sender.sendMessage("§cVocê não tem permissão para utilizar este rank.")
        }

        val rankLife = target.getRankLife(rank)
            ?: return sender.sendMessage("§cEste jogador não possui o rank ${rank.displayName}.")

        try {
            target.removeRank(rank)
            userManager.saveAll(target)

            apiBukkit.components.redisPool.resource.use {
                val json = HelixComponents.GSON.toJson(JsonObject().apply {
                    addProperty("type", "REMOVE")
                    addProperty("user", HelixComponents.GSON.toJson(target))
                    addProperty("rank-life", HelixComponents.GSON.toJson(rankLife))
                })
                it.publish("user-group-update", json)
            }

            sender.sendMessage("§aVocê removeu o rank ${rank.coloredName} §ade ${target.name}.")
        }catch (error: Exception) {
            error.printStackTrace()
            sender.sendMessage("§cOcorreu um erro ao remover o rank ${rank.displayName} do jogador ${target.name}.")
        }
    }
}