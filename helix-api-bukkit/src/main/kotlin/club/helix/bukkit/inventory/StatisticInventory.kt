package club.helix.bukkit.inventory

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.inventory.statistic.DuelsInventory
import club.helix.components.account.HelixUser
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class StatisticInventory(user: HelixUser) {

    private val inventory = HelixInventory.builder()
        .id("statistics")
        .title("Estatísticas")
        .size(3, 9)
        .provider(Provider(user))
        .build()

    fun open(player: Player) = inventory.open(player)

    class Provider(private val user: HelixUser): InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {


            contents.set(1, 1, ClickableItem.of(
                ItemBuilder()
                    .type(Material.DIAMOND_SWORD)
                    .displayName("§aDuels")
                    .lore("§eClique para ver mais.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) { DuelsInventory(user).open(player) } )

            contents.set(1, 2, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.IRON_CHESTPLATE)
                    .displayName("§aPvP")
                    .lore(
                        "§7Arena:",
                        "§7  Kills: §a${user.pvp.arena.kills}",
                        "§7  Deaths: §a${user.pvp.arena.deaths}",
                        "§7  Killstreak: §a${user.pvp.arena.killstreak}",
                        "§7  Max. Killstreak: §a${user.pvp.arena.maxKillStreak}",
                        "",
                        "§7FPS:",
                        "§7  Kills: §a${user.pvp.fps.kills.decimalFormat()}",
                        "§7  Deaths: §a${user.pvp.fps.deaths.decimalFormat()}",
                        "§7  Killstreak: §a${user.pvp.fps.killstreak.decimalFormat()}",
                        "§7  Max. Killstreak: §a${user.pvp.fps.maxKillStreak.decimalFormat()}",
                        "",
                        "§7Coins: §6${user.pvp.coins.decimalFormat()}"
                    )
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ))
            contents.set(1, 3, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.MUSHROOM_SOUP)
                    .displayName("§aHG")
                    .lore(
                        "§7Mix:",
                        "§7  Vitórias: §a${user.hg.mix.wins.decimalFormat()}",
                        "§7  Kills: §a${user.hg.mix.kills.decimalFormat()}",
                        "§7  Deaths: §a${user.hg.mix.deaths.decimalFormat()}",
                        "§7  Winstreak: §a${user.hg.mix.winstreak.decimalFormat()}",
                        "§7  Max. Winstreak: §a${user.hg.mix.maxWinStreak.decimalFormat()}",
                        "",
                        "§7Liga:",
                        "§7  Vitórias: §a${user.hg.league.wins.decimalFormat()}",
                        "§7  Kills: §a${user.hg.league.kills.decimalFormat()}",
                        "§7  Deaths: §a${user.hg.league.deaths.decimalFormat()}",
                        "§7  Winstreak: §a${user.hg.league.winstreak.decimalFormat()}",
                        "§7  Max. Winstreak: §a${user.hg.league.maxWinStreak.decimalFormat()}",
                        "",
                        "§7Coins: §6${user.hg.coins.decimalFormat()}"
                    )
                    .toItem
            ))
        }
    }
}