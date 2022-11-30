package club.helix.event.listener.config

import club.helix.bukkit.kotlin.player.Build.Companion.build
import club.helix.event.event.GameConfigChangeEvent
import club.helix.event.player.GamePlayerType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameBuildConfigListener: Listener {

    @EventHandler fun onGameConfigChange(event: GameConfigChangeEvent<Boolean>) = event.takeIf {
        it.configName.lowercase() == "build"
    }?.apply {
        game.getPlayers(GamePlayerType.PLAYING).forEach { it.player.build(value) }
    }
}