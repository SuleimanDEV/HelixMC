package club.helix.bukkit.inventory.statistic

import club.helix.components.account.HelixUser
import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.inventory.StatisticInventory
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class DuelsInventory(val user: HelixUser) {

    private val inventory = HelixInventory.builder()
        .id("statistic-duels")
        .title("Estatísticas do Duels")
        .size(4, 9)

    fun open(player: Player, backItem: Boolean = true) =
        inventory.provider(Provider(user, backItem)).build().open(player)


    class Provider(
        private val user: HelixUser,
        private val backItem: Boolean
    ): InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            if (backItem) {
                contents.set(3, 4, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aVoltar")
                        .toItem
                ) { StatisticInventory(user).open(player) } )
            }

            contents.set(1, 1, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.MUSHROOM_SOUP)
                    .displayName("§aSopa")
                    .lore(
                        "§7Partidas: §a${user.duels.soup.matches.decimalFormat()}",
                        "§7Vitórias: §a${user.duels.soup.wins.decimalFormat()}",
                        "§7Derrotas: §a${user.duels.soup.defeats.decimalFormat()}",
                        "§7Winstreak: §a${user.duels.soup.winstreak.decimalFormat()}",
                        "§7Max. Winstreak: §a${user.duels.soup.maxWinstreak.decimalFormat()}"
                    )
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ))

            contents.set(1, 2, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.IRON_FENCE)
                    .displayName("§aGladiator")
                    .lore(
                        "§7Partidas: §a${user.duels.gladiator.matches.decimalFormat()}",
                        "§7Vitórias: §a${user.duels.gladiator.wins.decimalFormat()}",
                        "§7Derrotas: §a${user.duels.gladiator.defeats.decimalFormat()}",
                        "§7Winstreak: §a${user.duels.gladiator.winstreak.decimalFormat()}",
                        "§7Max. Winstreak: §a${user.duels.gladiator.maxWinstreak.decimalFormat()}"
                    )
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ))

            contents.set(1, 3, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.POTION)
                    .data(37)
                    .displayName("§aNo Debuff")
                    .lore(
                        "§7Partidas: §a${user.duels.noDebuff.matches.decimalFormat()}",
                        "§7Vitórias: §a${user.duels.noDebuff.wins.decimalFormat()}",
                        "§7Derrotas: §a${user.duels.noDebuff.defeats.decimalFormat()}",
                        "§7Winstreak: §a${user.duels.noDebuff.winstreak.decimalFormat()}",
                        "§7Max. Winstreak: §a${user.duels.noDebuff.maxWinstreak.decimalFormat()}"
                    )
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ))

            contents.set(1, 4, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.LAVA_BUCKET)
                    .displayName("§aUHC")
                    .lore(
                        "§7Partidas: §a${user.duels.uhc.matches.decimalFormat()}",
                        "§7Vitórias: §a${user.duels.uhc.wins.decimalFormat()}",
                        "§7Derrotas: §a${user.duels.uhc.defeats.decimalFormat()}",
                        "§7Winstreak: §a${user.duels.uhc.winstreak.decimalFormat()}",
                        "§7Max. Winstreak: §a${user.duels.uhc.maxWinstreak.decimalFormat()}"
                    )
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ))

            contents.set(1, 5, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.FISHING_ROD)
                    .displayName("§aCombo")
                    .lore(
                        "§7Partidas: §a${user.duels.combo.matches.decimalFormat()}",
                        "§7Vitórias: §a${user.duels.combo.wins.decimalFormat()}",
                        "§7Derrotas: §a${user.duels.combo.defeats.decimalFormat()}",
                        "§7Winstreak: §a${user.duels.combo.winstreak.decimalFormat()}",
                        "§7Max. Winstreak: §a${user.duels.combo.maxWinstreak.decimalFormat()}"
                    )
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ))

            contents.set(1, 6, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.GOLDEN_APPLE)
                    .displayName("§aGapple")
                    .lore(
                        "§7Partidas: §a${user.duels.gapple.matches.decimalFormat()}",
                        "§7Vitórias: §a${user.duels.gapple.wins.decimalFormat()}",
                        "§7Derrotas: §a${user.duels.gapple.defeats.decimalFormat()}",
                        "§7Winstreak: §a${user.duels.gapple.winstreak.decimalFormat()}",
                        "§7Max. Winstreak: §a${user.duels.gapple.maxWinstreak.decimalFormat()}"
                    )
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ))

            contents.set(1, 7, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.LAVA_BUCKET)
                    .displayName("§aLava")
                    .lore(
                        "§7Partidas: §a${user.duels.lava.matches.decimalFormat()}",
                        "§7Vitórias: §a${user.duels.lava.wins.decimalFormat()}",
                        "§7Derrotas: §a${user.duels.lava.defeats.decimalFormat()}",
                        "§7Winstreak: §a${user.duels.lava.winstreak.decimalFormat()}",
                        "§7Max. Winstreak: §a${user.duels.lava.maxWinstreak.decimalFormat()}"
                    )
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ))

            contents.set(2, 1, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.APPLE)
                    .displayName("§aSumo")
                    .lore(
                        "§7Partidas: §a${user.duels.sumo.matches.decimalFormat()}",
                        "§7Vitórias: §a${user.duels.sumo.wins.decimalFormat()}",
                        "§7Derrotas: §a${user.duels.sumo.defeats.decimalFormat()}",
                        "§7Winstreak: §a${user.duels.sumo.winstreak.decimalFormat()}",
                        "§7Max. Winstreak: §a${user.duels.sumo.maxWinstreak.decimalFormat()}"
                    )
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ))

            contents.set(2, 2, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.STAINED_CLAY)
                    .data(11)
                    .displayName("§aThe Bridge")
                    .lore(
                        "§7Pontos: §a${user.duels.theBridge.points.decimalFormat()}",

                        "§7Partidas: §a${user.duels.theBridge.matches.decimalFormat()}",
                        "§7Vitórias: §a${user.duels.theBridge.wins.decimalFormat()}",
                        "§7Derrotas: §a${user.duels.theBridge.defeats.decimalFormat()}",
                        "§7Winstreak: §a${user.duels.theBridge.winstreak.decimalFormat()}",
                        "§7Max. Winstreak: §a${user.duels.theBridge.maxWinstreak.decimalFormat()}",
                    )
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ))
        }
    }
}