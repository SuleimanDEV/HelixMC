package club.helix.pvp.lava.inventory

import club.helix.bukkit.builder.ItemBuilder
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.pvp.lava.kotlin.player.lavaPlayer
import org.bukkit.Material
import org.bukkit.entity.Player

class SettingsInventory {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("settings")
            .title("Configurar")
            .size(3, 9)
            .provider(Provider())
            .build()

        fun open(player: Player) = run { inventory.open(player) }

        private class Provider: InventoryProvider {
            override fun init(player: Player, contents: InventoryContents) {
                contents.set(1, 1, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.FERMENTED_SPIDER_EYE)
                        .displayName("§aDano")
                        .lore(
                            "§7Atual: §c${player.lavaPlayer.lavaDamage / 2} ❤",
                            "§aClique para alterar."
                        )
                        .toItem
                ) { DamageSettingInventory.open(player) })
            }
        }
    }
}