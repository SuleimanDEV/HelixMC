package club.helix.duels.sumo.listener

import club.helix.bukkit.kotlin.player.teleport
import club.helix.duels.api.event.GameStartEvent
import club.helix.duels.api.game.DuelsPlayer
import club.helix.duels.sumo.SumoPlugin
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameStartListener(
    private val plugin: SumoPlugin
): Listener {

    @EventHandler fun onStart(event: GameStartEvent<*>) = event.game.apply {
        if (maxPlayers > 2) throw NullPointerException("invalid game players")

        playingPlayers.first().player.teleport(plugin.pos1)
        playingPlayers[1].player.teleport(plugin.pos2)

        playingPlayers.map(DuelsPlayer::player).map(Player::getInventory).forEach {
            it.clear()
            it.armorContents = null
        }
        broadcast("§aO jogo iniciou!")
    }
}