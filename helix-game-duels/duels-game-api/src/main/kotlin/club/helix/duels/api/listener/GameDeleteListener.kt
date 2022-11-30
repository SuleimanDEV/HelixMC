package club.helix.duels.api.listener

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.connect
import club.helix.duels.api.event.GameDeleteEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameDeleteListener: Listener {

    @EventHandler fun onGameDelete(event: GameDeleteEvent<*>) = event.game.apply {
        allPlayers().filter(Player::isOnline).forEach {
            val availableServer = HelixBukkit.instance.currentServer.findAvailableLobby()
                ?: return@apply it.kickPlayer("§cDUELS: Você foi expulso pois não há um lobby disponível.")
            it.sendMessage("§cO jogo acabou!")
            it.connect(availableServer)
        }
    }
}