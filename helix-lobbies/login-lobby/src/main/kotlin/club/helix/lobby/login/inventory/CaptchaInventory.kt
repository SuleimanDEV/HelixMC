package club.helix.lobby.login.inventory

import club.helix.bukkit.builder.ItemBuilder
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class CaptchaInventory {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("captcha")
            .size(3, 9)
            .closeable(false)

        val captchaPlayers get() = mutableListOf<String>()

        private val randomitems = arrayOf(
            ItemBuilder().type(Material.WORKBENCH).displayName("§aMesa de trabalho").toItem,
            ItemBuilder().type(Material.BLAZE_ROD).displayName("§aVara Incadescente").toItem,
            ItemBuilder().type(Material.EMERALD).displayName("§aEsmeralda").toItem,
            ItemBuilder().type(Material.COMPASS).displayName("§aBússola").toItem,
            ItemBuilder().type(Material.BOOK).displayName("§aLivro").toItem,
            ItemBuilder().type(Material.MUSHROOM_SOUP).displayName("§aSopa").toItem,
            ItemBuilder().type(Material.ANVIL).displayName("§aBigorna").toItem,
            ItemBuilder().type(Material.BED).displayName("§aCama").toItem,
            ItemBuilder().type(Material.CHEST).displayName("§aBaú").toItem,
        )

        fun open(player: Player) = Category.values().random().run {
            inventory.title(title)
                .provider(Provider(this))
                .build()
                .open(player)
            captchaPlayers.add(player.name)
        }
    }

    private enum class Category(
        val title: String,
        val correctItem: ItemStack
    ) {
        AXE("Selecione o machado", ItemBuilder().type(Material.WOOD_AXE)
            .displayName("§aMachado de Madeira").toItem),
        SWORD("Selecione a espada", ItemBuilder().type(Material.GOLD_SWORD)
            .displayName("§aEspada de Ouro").toItem),
        PICKAXE("Selecione a picareta", ItemBuilder().type(Material.DIAMOND_PICKAXE)
            .displayName("§aPicareta de Diamante").toItem),
        APPLE("Selecione a maçã", ItemBuilder().type(Material.APPLE)
            .displayName("§aMaçã").toItem);
    }

    private class Provider(private val category: Category): InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            val slots = mutableListOf(1, 3, 5, 7)
            val randomSlot = slots.random()

            contents.set(1, slots.removeAt(slots.indexOf(randomSlot)), ClickableItem.of(
                ItemBuilder()
                    .type(category.correctItem.type)
                    .displayName(category.correctItem.itemMeta?.displayName ?: "§c{error-name}")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) {
                contents.inventory().isCloseable = true
                it.whoClicked.closeInventory()
                captchaPlayers.remove(player.name)
            })

            val wrongItems = mutableListOf<ItemStack>()
            for (i in 0 until 3) {
                val randomItem = randomitems.filter { !wrongItems.contains(it) }.random()
                    .apply { wrongItems.add(this) }
                contents.set(1, slots.removeAt(slots.indexOf(slots.random())), ClickableItem.of(
                    ItemBuilder()
                        .type(randomItem.type)
                        .displayName(randomItem.itemMeta?.displayName ?: "§c{error-name}")
                        .itemFlags(*ItemFlag.values())
                        .toItem
                ) { player.kickPlayer("§cVocê errou o captcha.") })
            }
        }
    }
}