package club.helix.duels.lava.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.teleport
import club.helix.duels.api.event.GameStartEvent
import club.helix.duels.api.game.DuelsPlayer
import club.helix.duels.lava.LavaPlugin
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameStartListener(
    private val plugin: LavaPlugin
): Listener {

    @EventHandler fun onStart(event: GameStartEvent<*>) = event.game.apply {
        if (maxPlayers > 2) throw NullPointerException("invalid game players")

        playingPlayers.first().player.teleport(plugin.pos1)
        playingPlayers[1].player.teleport(plugin.pos2)

        playingPlayers.map(DuelsPlayer::player).map(Player::getInventory).forEach {
            it.clear()
            it.armorContents = null

            it.setItem(13, ItemBuilder().type(Material.BOWL).amount(64).toItem)
            it.setItem(14, ItemBuilder().type(Material.RED_MUSHROOM).amount(64).toItem)
            it.setItem(15, ItemBuilder().type(Material.BROWN_MUSHROOM).amount(64).toItem)

            for (i in 0..35) {
                it.addItem(ItemBuilder().type(Material.MUSHROOM_SOUP).toItem)
            }
        }
        broadcast("§aO jogo iniciou!")
    }
}