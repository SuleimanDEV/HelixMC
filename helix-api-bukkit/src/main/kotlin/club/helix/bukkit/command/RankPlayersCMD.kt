package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.HelixComponents
import club.helix.components.account.HelixRank
import club.helix.components.account.HelixRankLife
import club.helix.components.account.HelixUser
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.inv.content.SlotIterator
import kotlinx.serialization.decodeFromString
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.text.SimpleDateFormat

class RankPlayersCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("rank-players")
            .size(6, 9)

        private fun getTitle(rank: HelixRank, pageIndex: Int): String {
            val title = "${rank.displayName}: players (${pageIndex.plus(1)})"
            return title.takeIf { it.length <= 32 } ?: title.substring(0, 32)
        }
    }

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, HelixRank.values().map(HelixRank::displayName).toMutableList())
    )

    @CommandOptions(
        name = "rankplayers",
        target = CommandTarget.PLAYER,
        permission = true,
        description = "Verificar jogadores de um determinado rank."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            return sender.sendMessage("§cUtilize /rankplayers <rank> para visualizar os jogadores deste rank.")
        }

        val rank = HelixRank.get(args[0]) ?: return sender.sendMessage("§cRank não encontrado.")

        try {
            sender.sendMessage("§eCarregando jogadores...")
            val users = mutableListOf<HelixUser>()

            Bukkit.getScheduler().runTaskAsynchronously(apiBukkit) {
                apiBukkit.components.storage.newConnection.use {
                    it.query("select * from accounts where json_contains(data, '{\"rank\":\"$rank\"}', '$.ranksLife')")?.use { query ->
                        while (query.next()) {
                            val data = query.getString("data")
                            val user = HelixComponents.JSON.decodeFromString<HelixUser>(data)
                            users.add(user)
                        }
                    } ?: throw NullPointerException("invalid query")
                }

                if (users.isEmpty()) {
                    return@runTaskAsynchronously sender.sendMessage("§cNão há jogadores neste rank.")
                }
                inventory.title(getTitle(rank, 0)).provider(Provider(rank, users))
                    .build().open(sender.player)
            }
        }catch (error: Exception) {
            error.printStackTrace()
            sender.sendMessage("§cOcorreu um erro ao verificar os jogadores deste rank.")
        }
    }

    private class Provider(
        private val rank: HelixRank,
        private val users: MutableList<HelixUser>
    ): InventoryProvider {

        override fun init(player: Player, contents: InventoryContents) {
            val items = users.map { user ->
                val dateFormat = SimpleDateFormat("hh/MM/yyyy HH:mm")
                val rank = user.mainRankLife.rank

                ClickableItem.empty(
                    ItemBuilder()
                        .skull(user.name)
                        .displayName("${rank.color}${user.name}")
                        .lore(
                            "§7Ranks:",
                            *user.ranksLife.map(HelixRankLife::rank).sortedBy { it.ordinal }.map {
                                "§8- ${it.color}${it.displayName}"
                            }.toTypedArray(),
                            "§7Registro: ${dateFormat.format(user.login.firstLogin)}",
                            "§7Ultimo login: ${dateFormat.format(user.login.lastLogin)}"
                        )
                        .toItem
                )
            }

            contents.set(5, 4, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.BOOK)
                    .displayName("§aInformações gerais")
                    .lore(
                        "§7Rank: ${rank.color}${rank.displayName}",
                        "§7Jogadores: §f${users.size}"
                    )
                    .toItem
            ))

            val pagination = contents.pagination().apply {
                setItems(*items.toTypedArray())
                setItemsPerPage(21)
                addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1)
                    .blacklist(2, 0).blacklist(3, 0).blacklist(1, 8)
                    .blacklist(2, 8).blacklist(3, 8))
            }

            if (!pagination.isFirst) {
                contents.set(5, 0, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aPágina ${pagination.page}")
                        .toItem
                ) {
                    val previousPage = pagination.previous().page
                    inventory.title(getTitle(rank, previousPage)).provider(this)
                        .build().open(player, previousPage)
                })
            }

            if (!pagination.isLast) {
                contents.set(5, 8, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aPágina ${pagination.page.plus(1)}")
                        .toItem
                ) {
                    val nextPage = pagination.next().page
                    inventory.title(getTitle(rank, nextPage)).provider(this)
                        .build().open(player, nextPage)
                })
            }
        }
    }
}