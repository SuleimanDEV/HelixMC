package club.helix.duels.api.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.nms.NameTagNMS
import club.helix.duels.api.event.SpectatorJoinGameEvent
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class SpectatorJoinGameListener: Listener {

    @EventHandler fun onSpectarJoin(event: SpectatorJoinGameEvent) = event.apply {
        NameTagNMS.setNametag(player, "§7", 1, NameTagNMS.Reason.CUSTOM)

        player.apply {
            gameMode = GameMode.ADVENTURE
            canPickupItems = false
            allowFlight = true
            isFlying = true

            inventory.setItem(0, ItemBuilder()
                .type(Material.PAPER)
                .displayName("§aJogar novamente")
                .identify("cancel-drop")
                .identify("spectator-item", "play-again")
                .toItem
            )
            inventory.setItem(1, ItemBuilder()
                .type(Material.COMPASS)
                .displayName("§aJogadores")
                .identify("cancel-drop")
                .identify("spectator-item", "players")
                .toItem
            )

            inventory.setItem(8, ItemBuilder()
                .type(Material.BED)
                .displayName("§cVoltar ao lobby")
                .identify("cancel-drop")
                .identify("spectator-item", "back-to-lobby")
                .toItem
            )
        }
    }
}