package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.HelixComponents
import club.helix.components.account.HelixRank
import club.helix.components.account.HelixRankLife
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.fetcher.StringFetcher
import club.helix.components.server.HelixServer
import club.helix.components.util.HelixTimeFormat
import com.google.gson.JsonObject

class SetRankCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf<CommandCompleter>().apply {
        add(CommandCompleter(0, HelixServer.networkPlayers))

        val ranks = HelixRank.values().filter { sender.hasPermission("helix.cmd.rankset.${it.toString().lowercase()}") }
        add(CommandCompleter(1, ranks.map { it.displayName }.toMutableList()))
    }

    @CommandOptions(
        name = "setrank",
        description = "Alterar rank dos jogadores.",
        permission = true
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val availableRanks = HelixRank.values().filter {
            sender.hasPermission("helix.cmd.rankset.${it.toString().lowercase()}")
        }

        if (args.size < 2)
            return sender.sendMessage(arrayOf("§7Utilize /rankset <jogador> <rank> [tempo] para alterar o rank.",
                "§7Ranks disponíveis: ${availableRanks.joinToString(", ") { it.coloredName }}"))

        val userManager = apiBukkit.components.userManager
        val userTarget = (userManager.redisController.load(args[0])
            ?: userManager.userSqlController.load(args[0]))
            ?: return sender.sendMessage("§cEste jogador não está registrado no servidor.")

        val rank = HelixRank.get(args[1]) ?: return sender.sendMessage("§cRank não encontrado.")

        if (!availableRanks.contains(rank)) {
            return sender.sendMessage("§cVocê não tem permissão para utilizar este rank.")
        }

        val time = if (args.size >= 3) StringFetcher.getNumbers(args[2]).toLongOrNull()
            ?: return sender.sendMessage("§cNúmero inválido.") else 0
        val permanent = time == 0L

        val timeUnit = if (!permanent) HelixTimeFormat.getTimeUnit(StringFetcher.getLetters(args[2]))
            ?: return sender.sendMessage("§cTempo inválido.") else null

        if (rank.isBiggerThen(HelixRank.VIP) && rank.isLessThen(HelixRank.BETA) && permanent && !sender.hasPermission("helix.cmd.rankset.vip.permanent"))
            return sender.sendMessage("§cVocê não pode setar rank vip permanentemente.")

        if (HelixRank.vip(rank) && !HelixRank.staff(rank) && !permanent && timeUnit!!.toDays(time) > 7
            && !sender.hasPermission("helix.cmd.rankset.vip.unlimited"))
            return sender.sendMessage("§cVocê não pode setar rank vip por mais de 7 dias.")

        if (rank == HelixRank.DEFAULT && !permanent) {
            return sender.sendMessage("§cO rank padrão não pode ser setado temporariamente.")
        }

        userTarget.apply {
            if (!permanent) setRankTime(rank, time, timeUnit!!) else setRankPermanent(rank)
            userManager.saveAll(this)
        }

        val rankLife = HelixRankLife(rank, if (permanent) 0L else
            System.currentTimeMillis() + timeUnit!!.toMillis(time))

        apiBukkit.components.redisPool.resource.use {
            val json = HelixComponents.GSON.toJson(JsonObject().apply {

                addProperty("type", "SET")
                addProperty("user", HelixComponents.GSON.toJson(userTarget))
                addProperty("rank-life", HelixComponents.GSON.toJson(rankLife))
            })
            it.publish("user-group-update", json)
        }

        if (userTarget.name != sender.name) {
            sender.sendMessage("§eO rank de §b${userTarget.name} §efoi atualizado para ${rank.coloredName}§e. §7(${if (permanent) "Permanentemente" 
                else rankLife.formatTime()}")
        }
    }
}