package club.helix.event.listener

import club.helix.bukkit.event.PlayerDeathEvent
import club.helix.bukkit.kotlin.player.account
import club.helix.event.EventPlugin
import club.helix.event.player.GamePlayerType
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GamePlayerDeathListener(private val plugin: EventPlugin): Listener {

    @EventHandler fun onDeath(event: PlayerDeathEvent) = event.apply {
        if (plugin.game.getConfig<Boolean>("drop-items-on-death") == false) {
            drops.clear()
        }

        if (hasKiller()) {
            player.sendMessage("§cVocê morreu para ${killer.name}.")
            killer.sendMessage("§aVocê matou ${player.name}.")

            if (plugin.game.getConfig<Boolean>("death-message") == true) {
                Bukkit.broadcastMessage("${player.account.tag.color}${player.name} §7foi morto por ${killer.account.tag.color}${killer.name}§7.")
            }
        }

        plugin.game.putPlayer(player, GamePlayerType.SPECTATOR)
    }
}