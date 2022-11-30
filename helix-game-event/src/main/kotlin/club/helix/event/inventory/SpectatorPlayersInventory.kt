package club.helix.event.inventory

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.event.player.GamePlayer
import club.helix.event.player.GamePlayerType
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.inv.content.SlotIterator
import org.bukkit.Material
import org.bukkit.entity.Player
import java.text.DecimalFormat

class SpectatorPlayersInventory(private val players: MutableList<GamePlayer>) {
    companion object {
        private val INVENTORY = HelixInventory.builder()
            .id("spectator-players")
            .title("Jogadores")
            .size(3, 9)
    }

    fun open(player: Player) = INVENTORY.provider(Provider(players))
        .build().open(player)

    private class Provider(
        private val players: MutableList<GamePlayer>
    ): InventoryProvider {

        override fun init(player: Player, contents: InventoryContents) {
            val items = players.filter { it.type == GamePlayerType.PLAYING }.map { gamePlayer ->
                ClickableItem.of(
                    ItemBuilder()
                        .customSkull(gamePlayer.player.name)
                        .displayName(gamePlayer.player.account.tag.color + gamePlayer.player.name)
                        .lore(
                            "§7Vida: §c${DecimalFormat("#.#").format(gamePlayer.player.health / 2)} ❤",
                            "§eClique para teleportar."
                        )
                        .toItem
                ) { it.whoClicked.teleport(gamePlayer.player) }
            }

            val pagination = contents.pagination().apply {
                setItems(*items.toTypedArray())
                setItemsPerPage(7)
                addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1)
                    .blacklist(2, 0).blacklist(3, 0).blacklist(1, 8)
                    .blacklist(2, 8).blacklist(3, 8))
            }

            if (!pagination.isFirst) {
                contents.set(2, 0, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aPagina ${pagination.page}")
                        .toItem
                ) {
                    INVENTORY.provider(this).build().open(player, pagination.previous().page)
                } )
            }

            if (!pagination.isLast) {
                contents.set(2, 8, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aPagina ${pagination.page.plus(2)}")
                        .toItem
                ) { INVENTORY.provider(this).build().open(player, pagination.next().page) } )
            }
        }
    }
}