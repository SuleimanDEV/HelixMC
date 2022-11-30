package club.helix.duels.api.listener

import club.helix.duels.api.event.StartingStateTaskEvent
import club.helix.duels.api.game.DuelsGameState
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class StartingTimerListener: Listener {

    @EventHandler fun onTimer(event: StartingStateTaskEvent) = event.game.apply {
        if (playingPlayers.size < maxPlayers && time != 5) {
            time = 5
            changeState(DuelsGameState.WAITING)
            return@apply broadcast("§cO temporizador reiniciou por falta de jogadores.")
        }

        if (time in 1..5) {
            playSound(Sound.CLICK)
            broadcast("§eIniciando jogo em §c$time ${if (time > 1) "segundos" else
                "segundo"}§e.")
        }
    }
}