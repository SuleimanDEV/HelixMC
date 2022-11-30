package club.helix.lobby.provider.util

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.account
import org.bukkit.Material
import org.bukkit.entity.Player

class SpawnItems {
    companion object {
        fun set(player: Player): Unit = player.inventory.run {
            setItem(0, ItemBuilder()
                .type(Material.COMPASS)
                .displayName("§aSelecionar jogo")
                .identify("lobby-item", "select-game")
                .identify("cancel-click")
                .identify("cancel-drop")
                .toItem
            )

            setItem(1, ItemBuilder()
                .skull(player.name)
                .displayName("§aMeu perfil")
                .identify("lobby-item", "profile")
                .identify("cancel-click")
                .identify("cancel-drop")
                .toItem
            )

            setItem(4, ItemBuilder()
                .type(Material.RAW_FISH)
                .data(3)
                .displayName("§aColecionáveis")
                .identify("lobby-item", "collectibles")
                .identify("cancel-click")
                .identify("cancel-drop")
                .toItem
            )

            val visible = player.account.preferences.lobby.playersVisible
            setItem(7, ItemBuilder()
                .type(Material.INK_SACK)
                .data(if (visible) 10 else 8)
                .displayName("§fJogadores: ${if (visible) "§aON" else "§cOFF"}")
                .identify("lobby-item", "visible-players")
                .identify("cancel-click")
                .identify("cancel-drop")
                .toItem
            )

            setItem(8, ItemBuilder()
                .type(Material.NETHER_STAR)
                .displayName("§aSelecionar lobby")
                .identify("lobby-item", "select-lobby")
                .identify("cancel-click")
                .identify("cancel-drop")
                .toItem
            )
            player.updateInventory()
        }
    }
}