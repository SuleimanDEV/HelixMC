package club.helix.pvp.lava.inventory

import club.helix.bukkit.builder.ItemBuilder
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.pvp.lava.kotlin.player.lavaPlayer
import org.bukkit.Material
import org.bukkit.entity.Player

class DamageSettingInventory {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("settings")
            .title("Configurar")
            .size(3, 9)
            .provider(Provider())
            .build()

        fun open(player: Player) = run { inventory.open(player) }
    }

    private class Provider: InventoryProvider {
        fun changeDamage(player: Player, newDamage: Double) {
            player.lavaPlayer.lavaDamage = newDamage
            player.sendMessage("§aDano padrão da lava alterado para §c${newDamage / 2} ❤§a.")
        }

        override fun init(player: Player, contents: InventoryContents) {
            contents.set(1, 1, ClickableItem.of(
                ItemBuilder()
                    .type(Material.STAINED_GLASS_PANE)
                    .data(5)
                    .displayName("§aFácil")
                    .lore("§c1.0 ❤")
                    .toItem
            ) { changeDamage(player, 2.0) })

            contents.set(1, 2, ClickableItem.of(
                ItemBuilder()
                    .type(Material.STAINED_GLASS_PANE)
                    .data(4)
                    .displayName("§eMédio")
                    .lore("§c2.0 ❤")
                    .toItem
            ) { changeDamage(player, 4.0) })

            contents.set(1, 3, ClickableItem.of(
                ItemBuilder()
                    .type(Material.STAINED_GLASS_PANE)
                    .data(14)
                    .displayName("§cDifícil")
                    .lore("§c3.0 ❤")
                    .toItem
            ) { changeDamage(player, 6.0) })

            contents.set(1, 4, ClickableItem.of(
                ItemBuilder()
                    .type(Material.STAINED_GLASS_PANE)
                    .data(15)
                    .displayName("§4Extremo")
                    .lore("§c4.0 ❤")
                    .toItem
            ) { changeDamage(player, 8.0) })

            contents.set(2, 4, ClickableItem.of(
                ItemBuilder()
                    .type(Material.ARROW)
                    .displayName("§aVoltar")
                    .toItem
            ) { SettingsInventory.open(player) })
        }
    }
}