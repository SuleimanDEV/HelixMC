package club.helix.duels.api.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.duels.api.DuelsAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class PlayAgainItemListener(private val duelsAPI: DuelsAPI<*>): Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.hasItem() && ItemBuilder.has(event.item, "spectator-item", "play-again")
    }?.apply {
        isCancelled = true
        duelsAPI.findGame(player)?.apply { removePlayer(player) }

        val newGame = duelsAPI.newGame()
        newGame.addPlayer(player)

        player.sendMessage("§aEnviado para um novo jogo!")
    }
}