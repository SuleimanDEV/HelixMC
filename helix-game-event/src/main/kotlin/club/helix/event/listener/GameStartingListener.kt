package club.helix.event.listener

import club.helix.event.EventGame
import club.helix.event.event.GameStateCallEvent
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameStartingListener: Listener {

    @EventHandler fun onGameStateCall(event: GameStateCallEvent) = event.takeIf {
        it.game.state == EventGame.State.WAITING && it.game.time.let { time ->
            time == 50 || time == 30 || time == 15 || time == 10 ||
                    (time <= 5 && time != 0)
        }
    }?.game?.apply {
        playSound(Sound.CLICK)
        Bukkit.broadcastMessage("§eIniciando em §b$time ${if (time > 1) "segundos" else "segundo"}§e.")
    }
}