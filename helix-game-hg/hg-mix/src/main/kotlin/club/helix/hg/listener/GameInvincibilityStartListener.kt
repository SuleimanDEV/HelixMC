package club.helix.hg.listener

import club.helix.components.server.provider.HardcoreGamesProvider
import club.helix.hg.player.event.GameStateChangeEvent
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameInvincibilityStartListener: Listener {

    @EventHandler fun onGameChangeState(event: GameStateChangeEvent) = event.takeIf {
        it.game.state == HardcoreGamesProvider.State.INVINCIBILITY
    }?.apply {
        game.playSound(Sound.ENDERDRAGON_GROWL)
        Bukkit.broadcastMessage("§6A invencibilidade começou!")
    }
}