package club.helix.lobby.hg.inventory

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.connect
import club.helix.components.server.HelixServer
import club.helix.components.server.provider.HardcoreGamesProvider
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.inv.content.SlotIterator
import org.bukkit.Material
import org.bukkit.entity.Player

class HgMixInventory {
    companion object {
        private val inventory get() = HelixInventory.builder()
            .id("hg-mix")
            .title("Selecionar sala")
            .size(3, 9)
            .provider(Provider())
            .build()

        fun open(player: Player) = inventory.open(player)
    }

    class Provider: InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            val hgRooms = HelixServer.HG.providers<HardcoreGamesProvider>(HelixServer.Category.HG_MIX)

            player.sendMessage("§e...")
            player.sendMessage(hgRooms.map { it.displayName }.toTypedArray())

            if (hgRooms.isEmpty()) {
                contents.set(1, 4, ClickableItem.empty(
                    ItemBuilder()
                        .type(Material.BARRIER)
                        .displayName("§cNão há salas disponíveis!")
                        .toItem
                ))
                return
            }

            val items = hgRooms.map { server ->
                ClickableItem.of(
                    ItemBuilder()
                        .type(Material.STAINED_GLASS_PANE)
                        .data(5)
                        .displayName("§a${server.displayName}")
                        .lore(
                            when (server.state) {
                                HardcoreGamesProvider.State.WAITING ->
                                    "§aAguardando jogadores..."
                                HardcoreGamesProvider.State.INVINCIBILITY ->
                                    "§eInvencibilidade acaba em ${server.time} ${if (server.time > 1) "segundos" else "segundo"}."
                                HardcoreGamesProvider.State.PLAYING ->
                                    "§cEm jogo há ${if (server.time <= 60) "${server.time} segundos" else "${server.time / 60} minutos."}"
                                else -> "§cSala offline."
                            },
                            "§fJogadores: §a${server.onlinePlayers.size}/${server.maxPlayers}"
                        )
                        .toItem,
                ) {
                    if (!server.online) {
                        return@of it.whoClicked.sendMessage("§cEste servidor está offline.")
                    }
                    if (!server.available) return@of

                    it.whoClicked.sendMessage("§aConectando em ${server.displayName}...")
                    (it.whoClicked as Player).connect(server)
                }
            }

            contents.pagination().apply {
                setItems(*items.toTypedArray())
                setItemsPerPage(7)
                addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1))
            }
        }
    }
}