package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.HelixComponents
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.server.HelixServer
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.inv.content.SlotIterator
import org.bukkit.Material
import org.bukkit.entity.Player

class OnlinePlayersCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("online-players")
            .title("Jogadores Online")
            .size(6, 9)
    }

    @CommandOptions(
        name = "onlineplayers",
        target = CommandTarget.PLAYER,
        permission = true,
        description = "Visualizar jogadores online."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        try {
            inventory.provider(Provider(apiBukkit.components)).build().open(sender.player)
        }catch (error: Exception) {
            error.printStackTrace()
            sender.sendMessage("§cOcorreu um erro ao abrir o inventário de jogadores.")
        }
    }

    private class Provider(private val components: HelixComponents): InventoryProvider {

        override fun init(player: Player, contents: InventoryContents) {
            val items = HelixServer.networkPlayers.map { username ->
                val user = components.userManager.redisController.load(username)
                    ?: throw NullPointerException("invalid user")
                val server = HelixServer.getPlayerServer(username)
                    ?: throw NullPointerException("invalid server")
                val rank = user.mainRankLife.rank

                ClickableItem.of(
                    ItemBuilder()
                        .skull(username)
                        .displayName("${rank.color}$username")
                        .lore(
                            "§7Rank: ${rank.color}${rank.displayName}",
                            "§7Servidor: §e${server.displayName} (${server.serverName})",
                            "§aClique para teleportar."
                        )
                        .toItem
                ) { player.performCommand("goto $username") }
            }

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
                    inventory.provider(this).build().open(player, pagination.previous().page)
                })
            }

            if (!pagination.isLast) {
                contents.set(5, 8, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aPágina ${pagination.page.plus(1)}")
                        .toItem
                ) {
                    inventory.provider(this).build().open(player, pagination.next().page)
                })
            }
        }
    }
}