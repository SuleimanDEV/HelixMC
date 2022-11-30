package club.helix.duels.api.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.duels.api.DuelsAPI
import club.helix.duels.api.game.DuelsGame
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.inv.content.SlotIterator
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class PlayersItemListener(private val duelsAPI: DuelsAPI<*>): Listener {

    private val inventory = HelixInventory.builder()
        .id("spectator-players")
        .title("Jogadores")
        .size(3, 9)

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(event.item, "spectator-item", "players")
    }?.apply {
        isCancelled = true

        val game = duelsAPI.findGame(player)
            ?: throw NullPointerException("invalid game")

        inventory.provider(Provider(game)).build().open(player)
    }

    private class Provider(
        private val game: DuelsGame
    ): InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            val items = mutableListOf<ClickableItem>()

            game.playingPlayers.forEach { duelsPlayer ->
                items.add(ClickableItem.of(
                    ItemBuilder()
                        .skull(duelsPlayer.player.name)
                        .displayName(duelsPlayer.teamColor.color + duelsPlayer.player.name)
                        .lore("§7Vida: §c${duelsPlayer.player.health / 2}")
                        .toItem
                ) { it.whoClicked.teleport(duelsPlayer.player) })
            }

            contents.pagination().apply {
                setItemsPerPage(7)
                setItems(*items.toTypedArray())
                addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1))
            }
        }
    }
}