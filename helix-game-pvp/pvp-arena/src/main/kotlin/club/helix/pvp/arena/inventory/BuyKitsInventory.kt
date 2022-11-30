package club.helix.pvp.arena.inventory

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.kotlin.player.sendPlayerTitle
import club.helix.components.HelixComponents
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.inv.content.SlotIterator
import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.kit.ArenaKit
import club.helix.pvp.arena.kotlin.player.hasKitPermission
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class BuyKitsInventory(private val api: HelixComponents) {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("buy-kits")
            .title("Comprar kits")
            .size(6, 9)
    }

    fun open(player: Player, page: Int = 0) = inventory.provider(Provider(api)).build().open(player, page)

    private class Provider(private val api: HelixComponents): InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            val kits = ArenaKit.values().filter { it.price > 0 && !player.hasKitPermission(it) }

            if (kits.isEmpty()) {
                contents.set(3, 4, ClickableItem.empty(
                    ItemBuilder()
                        .type(Material.STAINED_GLASS_PANE)
                        .data(14)
                        .displayName("§cVocê já possui todos os kits.")
                        .toItem
                ))
                return
            }
            val inventoryBuilder = inventory.provider(this).build()
            val user = player.account
            val pvp = user.pvp
            val items = mutableListOf<ClickableItem>()

            kits.forEach { kit ->
                val canBuy = pvp.coins >= kit.price

                items.add(ClickableItem.of(
                    ItemBuilder()
                        .type(kit.icon.type)
                        .displayName("${if (canBuy) "§a" else "§c"}${kit.displayName}")
                        .lore(
                            *kit.description.map { "§7$it" }.toTypedArray(),
                            "",
                            "§7Preço: §6${kit.price.decimalFormat()} coins",
                            "",
                            if (canBuy) "§aClique para comprar." else "§cVocê não possui coins o suficiente."
                        )
                        .itemFlags(*ItemFlag.values())
                        .toItem
                ) {
                    if (!canBuy) return@of
                    pvp.coins -= kit.price
                    api.userManager.saveAll(user)

                    PvPArena.instance.arenaUserKits.apply {
                        addKit(it.whoClicked.name, kit)
                        save(it.whoClicked.name)
                    }

                    it.whoClicked.closeInventory()
                    player.sendPlayerTitle("§e${kit.displayName}", "§fAdquirido por §6${kit.price} coins§f!")
                    it.whoClicked.sendMessage("§aVocê comprou o kit ${kit.displayName} por §6${kit.price.decimalFormat()} coins§a!")
                })
            }

            contents.set(5, 4, ClickableItem.empty(
                ItemBuilder()
                    .type(Material.EMERALD)
                    .displayName("§7Seus coins: §6${pvp.coins}")
                    .toItem
            ))

            val pagination = contents.pagination().apply {
                setItemsPerPage(28)
                setItems(*items.toTypedArray())
                addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1)
                    .blacklist(2, 0).blacklist(3, 0).blacklist(4, 0)
                    .blacklist(1, 8).blacklist(2, 8).blacklist(3, 8))
            }

            if (!pagination.isFirst) {
                contents.set(5, 0, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aPágina ${pagination.page}")
                        .toItem
                ) { inventoryBuilder.open(player, pagination.previous().page) })
            }

            if (!pagination.isLast) {
                contents.set(5, 8, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aPágina ${pagination.page.plus(1)}")
                        .toItem
                ) { inventoryBuilder.open(player, pagination.next().page) })
            }
        }
    }
}