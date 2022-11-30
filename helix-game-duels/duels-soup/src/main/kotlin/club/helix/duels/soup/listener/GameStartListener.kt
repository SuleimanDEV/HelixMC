package club.helix.duels.soup.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.teleport
import club.helix.duels.api.event.GameStartEvent
import club.helix.duels.api.game.DuelsPlayer
import club.helix.duels.soup.SoupPlugin
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemFlag

class GameStartListener(
    private val plugin: SoupPlugin
): Listener {

    @EventHandler fun onStart(event: GameStartEvent<*>) = event.game.apply {
        if (maxPlayers > 2) throw NullPointerException("invalid game players")

        playingPlayers.first().player.teleport(plugin.pos1)
        playingPlayers[1].player.teleport(plugin.pos2)

        playingPlayers.map(DuelsPlayer::player).map(Player::getInventory).forEach {
            it.clear()
            it.armorContents = null

            it.setItem(0, ItemBuilder()
                .type(Material.STONE_SWORD)
                .displayName("§7Espada de Pedra")
                .identify("cancel-drop")
                .unbreakable()
                .itemFlags(*ItemFlag.values())
                .toItem
            )

            for (i in 1..8) {
                it.addItem(ItemBuilder().type(Material.MUSHROOM_SOUP).toItem)
            }
        }
        broadcast("§aO jogo iniciou!")
    }
}