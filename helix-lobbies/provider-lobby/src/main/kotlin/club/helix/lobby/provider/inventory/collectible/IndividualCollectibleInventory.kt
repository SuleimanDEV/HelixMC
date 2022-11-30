package club.helix.lobby.provider.inventory.collectible

import club.helix.bukkit.builder.ItemBuilder
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.inv.content.SlotIterator
import club.helix.lobby.provider.collectible.Collectible
import club.helix.lobby.provider.collectible.CollectibleCategory
import club.helix.lobby.provider.collectible.CollectibleManager
import org.bukkit.Material
import org.bukkit.entity.Player

class IndividualCollectibleInventory(
    private val collectibleManager: CollectibleManager,
    private val category: CollectibleCategory
) {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("collectibles")
            .title("Colecionáveis")
            .size(6, 9)
    }

    fun open(player: Player) = inventory.provider(Provider(collectibleManager, category))
        .build().open(player)

    private class Provider(
        private val collectibleManager: CollectibleManager,
        private val category: CollectibleCategory
    ): InventoryProvider {

        override fun init(player: Player, contents: InventoryContents) {

            fun handleCollectible(collectible: Collectible) {
                if (!collectibleManager.hasCollectiblePermission(player, collectible)) return
                val selected = collectibleManager.hasSelectedCollectible(player, collectible)

                if (selected) {
                    collectibleManager.removeCollectible(player, collectible.apply { onDisable(player) })
                }else {
                    collectibleManager.addCollectible(player, collectible.apply { onEnable(player) })
                }
                inventory.provider(this).build().open(player, contents.pagination().page)

                collectibleManager.getSelectedCollectibles(player).takeIf {
                    it.isNotEmpty()
                }?.let {
                    collectibleManager.userController.save(player, it)
                } ?: collectibleManager.userController.delete(player)
            }

            val items = collectibleManager.getCollectibles(category).sortedBy {
                !collectibleManager.getCollectiblesPermission(player).contains(it)
            }.sortedBy { System.currentTimeMillis() - it.createdAt }.map { collectible ->
                val hasCollectible = collectibleManager.hasCollectiblePermission(player, collectible)
                val selected = collectibleManager.hasSelectedCollectible(player, collectible)

                val status = when {
                    hasCollectible && selected -> "§cSelecionado."
                    !hasCollectible -> "§cVocê não possui."
                    else -> "§7Clique para ativar."
                }

                ClickableItem.of(
                    ItemBuilder(collectible.icon.clone())
                        .displayName("${if (hasCollectible) "§a"
                            else "§c"}${collectible.name}${if (collectible.isNew()) " §e§lNOVO!" else ""}")
                        .lore(status)
                        .toItem
                ) { handleCollectible(collectible) }
            }

            val pagination = contents.pagination().apply {
                setItems(*items.toTypedArray())
                setItemsPerPage(21)
                addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1)
                    .blacklist(2, 0).blacklist(3, 0).blacklist(1, 8)
                    .blacklist(2, 8).blacklist(3, 8))
            }

            val selectedCollectible = collectibleManager.getSelectedCollectible<Collectible>(player, category)

            contents.set(5, 5, ClickableItem.empty(
                (selectedCollectible?.icon?.clone()?.run { ItemBuilder(this) }
                    ?: ItemBuilder().customSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMThiZTE0N2RmNDkwOTE3MzY5NjRkYWIzNTY5YjYxYzllNmEzNDFjOWE0OGY0YWY4NGI1MWE2M2JiNmU4NSJ9fX0="))
                    .displayName(selectedCollectible?.run { "§7Selecionado: §a$name" } ?: "§eNenhum selecionado.")
                    .toItem
            ))

            contents.set(5, 3, ClickableItem.of(
                ItemBuilder()
                    .type(Material.BARRIER)
                    .displayName("§cRemover")
                    .toItem
            ) { selectedCollectible?.let { handleCollectible(it) } })

            contents.set(5, 4, ClickableItem.of(
                ItemBuilder()
                    .type(Material.ARROW)
                    .displayName("§aVoltar")
                    .toItem
            ) { CollectiblesInventory(collectibleManager).open(player)} )

            if (!pagination.isFirst) {
                contents.set(2, 0, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aPagina ${pagination.page}")
                        .toItem
                ) {
                    inventory.provider(this).build().open(player, pagination.previous().page)
                } )
            }

            if (!pagination.isLast) {
                contents.set(2, 8, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aPagina ${pagination.page.plus(2)}")
                        .toItem
                ) { inventory.provider(this).build().open(player, pagination.next().page) } )
            }
        }
    }
}