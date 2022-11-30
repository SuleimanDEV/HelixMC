package club.helix.event.listener

import club.helix.event.EventGame
import club.helix.event.event.GameStateChangeEvent
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameStartListener: Listener {

    @EventHandler fun onGameChangeState(event: GameStateChangeEvent) = event.takeIf {
        it.game.state == EventGame.State.PLAYING
    }?.apply {
        game.playSound(Sound.ENDERDRAGON_GROWL)
        Bukkit.broadcastMessage("§aO evento iniciou!")
    }
}