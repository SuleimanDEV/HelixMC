package club.helix.duels.api.listener

import club.helix.bukkit.nms.NameTagNMS
import club.helix.duels.api.event.GameStartEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameStartListener: Listener {

    @EventHandler fun onGameStart(event: GameStartEvent<*>) = event.game.playingPlayers.forEach {
        NameTagNMS.setNametag(it.player, it.teamColor.color, 0, NameTagNMS.Reason.CUSTOM)
    }
}