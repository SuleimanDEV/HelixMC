package club.helix.lobby.provider.inventory.collectible

import club.helix.bukkit.builder.ItemBuilder
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.lobby.provider.collectible.CollectibleCategory
import club.helix.lobby.provider.collectible.CollectibleManager
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class CollectiblesInventory(collectibleManager: CollectibleManager) {

    private val inventory = HelixInventory.builder()
        .id("collectibles")
        .title("Colecionáveis")
        .size(3, 9)
        .provider(Provider(collectibleManager))
        .build()

    fun open(player: Player) = inventory.open(player)

    class Provider(private val collectibleManager: CollectibleManager): InventoryProvider {

        override fun init(player: Player, contents: InventoryContents) {

            fun add(row: Int,
                    column: Int,
                    category: CollectibleCategory,
                    description: Array<String>,
                    icon: ItemStack
            ) {
                contents.set(row, column, ClickableItem.of(
                    ItemBuilder(icon)
                        .lore(
                            *description.map { "§7$it" }.toTypedArray(),
                            "",
                            "§c‣ §7Desbloqueados: §c${collectibleManager.getCollectiblesPermission(player, category).size}/${collectibleManager.getCollectibles(category).size}",
                            "",
                            "§eClique para ver."
                        )
                        .toItem,
                ) { IndividualCollectibleInventory(collectibleManager, category).open(player) } )
            }

            add(1, 2, CollectibleCategory.HAT,
                arrayOf("Diversos chápeus para dar", "um toque no seu visual."),
                ItemBuilder()
                    .customSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGIzMTA3NDc5NjlkYjc0OWViMmNiZjk2OGRlNjcyNjQzMGQ3YTBhZGE1OTNhMmU2Mjc0MzQ0MDY3NTNkNjRlYiJ9fX0=")
                    .displayName("§aChapéus")
                    .toItem
            )

            add(1, 4, CollectibleCategory.CONTRAPTION,
                arrayOf("Escolha uma engenhoca para", "se divertir em nossos lobbies."),
                ItemBuilder()
                    .type(Material.ARMOR_STAND)
                    .displayName("§aEngenhocas")
                    .toItem
            )

            add(1, 6, CollectibleCategory.PARTICLE,
                arrayOf(),
                ItemBuilder()
                    .type(Material.BREWING_STAND_ITEM)
                    .displayName("§aParticulas")
                    .toItem
            )
        }
    }
}