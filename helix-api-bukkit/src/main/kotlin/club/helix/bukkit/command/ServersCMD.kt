package club.helix.bukkit.command

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.server.HelixServer
import club.helix.components.server.HelixServerProvider
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.inv.content.SlotIterator
import org.bukkit.Material
import org.bukkit.entity.Player
import kotlin.math.round

class ServersCMD: BukkitCommandExecutor() {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("servers")
            .title("Administração: Servidores")
            .size(6, 9)
            .provider(Provider())
            .build()
    }

    @CommandOptions(
        name = "servers",
        target = CommandTarget.PLAYER,
        permission = true,
        description = "Visualizar servidores."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>): Unit =
         run { inventory.open(sender.player) }

    private class Provider: InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            val items = mutableListOf<ClickableItem>()
            val servers = mutableListOf<HelixServerProvider>()

            HelixServer.values().forEach { server ->
                server.providers.values.sortedByDescending {
                    it.category == HelixServer.Category.LOBBY
                }.forEach { provider ->
                    servers.add(provider)
                    val status = when {
                        provider.online && provider.available -> "§aOnline e Disponível"
                        provider.online && !provider.available -> "§eOnline e Indisponível"
                        else -> "§cOffline"
                    }

                    items.add(ClickableItem.of(
                        ItemBuilder()
                            .type(Material.DARK_OAK_DOOR_ITEM)
                            .displayName("${if (provider.online) "§a" else "§c"}${provider.displayName}")
                            .lore(
                                "§7Status: $status",
                                "§7Identidade: §e${provider.serverName}",
                                "§7Online: ${if (provider.online) "§a${provider.onlinePlayers.size}/${provider.maxPlayers}" else "§c0/0"}"
                            )
                            .toItem
                    ) {
                        if (!provider.online) return@of
                        player.connect(provider, true)
                    })
                }
            }
            val onlineServers = servers.filter { it.online }.size
            val onlineServersPercentage = round((onlineServers.toDouble() / servers.size.toDouble()) * 100).toInt()
            val offlineServers = servers.filter { !it.online }.size
            val offlineServersPercentage = round((offlineServers.toDouble() / servers.size.toDouble()) * 100).toInt()

            contents.set(5, 4, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.BOOK)
                    .displayName("§aInformações gerais")
                    .lore(
                        "§7Total: §a${servers.size}",
                        "§7Online: §a$onlineServers §7($onlineServersPercentage%)",
                        "§7Offline: §c$offlineServers §7($offlineServersPercentage%)"
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

            val currentPage = pagination.page
            var previousPage = pagination.previous().page
            var nextPage = pagination.next().page

            if (currentPage != 0) {
                contents.set(5, 0, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aPágina ${previousPage.plus(1)}")
                        .toItem
                ) {
                    previousPage = pagination.previous().page
                    inventory.open(player, previousPage)
                })
            }

            if (currentPage != pagination.last().page) {
                contents.set(5, 8, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aPágina $nextPage")
                        .toItem
                ) {
                    nextPage = pagination.next().page
                    inventory.open(player, nextPage)
                })
            }
        }
    }
}