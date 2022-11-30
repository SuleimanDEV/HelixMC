package club.helix.pvp.arena.inventory

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.sendPlayerTitle
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.inv.content.SlotIterator
import club.helix.pvp.arena.ArenaPlayer
import club.helix.pvp.arena.kit.ArenaKit
import club.helix.pvp.arena.kotlin.player.hasKitPermission
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class KitsInventory {
    companion object {
        private val inventory = HelixInventory.builder()
            .title("Selecionar kit")
            .id("select-kit")
            .size(6, 9)

        fun open(player: Player, arenaPlayer: ArenaPlayer, kitNumber: Int) =
            inventory.provider(Provider(arenaPlayer, kitNumber)).build().open(player)

        private class Provider(
            private val arenaPlayer: ArenaPlayer,
            private val kitNumber: Int
        ): InventoryProvider {

            override fun init(player: Player, contents: InventoryContents) {
                val items = ArenaKit.values().sortedBy { it.handler == null && it != ArenaKit.NONE }.sortedBy {
                    !player.hasKitPermission(it)
                }.map { kit ->
                    val kitPermission = kitNumber == 1 || player.hasKitPermission(kit)
                    val footer = (if (!arenaPlayer.hasSelectedKit(kitNumber, kit)) "§aClique para selecionar" else
                        "§cSelecionado.").takeIf { kitPermission } ?: "§cVocê não possui este kit."


                    ClickableItem.of(
                        ItemBuilder()
                            .type(if (kitPermission) kit.icon.type else Material.STAINED_GLASS_PANE)
                            .data(if (kitPermission) kit.icon.durability.toInt() else 14)
                            .displayName("${if (kitPermission) "§a" else "§c"}${kit.displayName}${if (kit != ArenaKit.NONE && kit.handler == null) " §3§lEM BREVE!" else ""}")
                            .lore(
                                *kit.description.map { "§7$it" }.toTypedArray(),
                                "",
                                footer
                            )
                            .itemFlags(*ItemFlag.values())
                            .toItem
                    ) {
                        if (kit.handler ==  null && kit != ArenaKit.NONE) return@of

                        if (!kitPermission) {
                            return@of player.sendMessage("§cVocê não possui este kit.")
                        }

                        if (kit != ArenaKit.NONE && arenaPlayer.hasSelectedKit(kit))  {
                            return@of player.sendMessage("§cVocê já selecionou este kit.")
                        }

                        arenaPlayer.selectedKits.values.takeIf { selectedKits ->
                            selectedKits.any { selectedKit ->
                                arenaPlayer.hasSelectedRemainingKit(kitNumber, selectedKit) &&
                                        selectedKit.conflicts.any {
                                                conflict -> conflict.lowercase() == kit.toString().lowercase()
                                        }
                            } || selectedKits.any { selectedKit ->
                                arenaPlayer.hasSelectedRemainingKit(kitNumber, selectedKit) &&
                                        kit.conflicts.any {
                                            conflict -> conflict.lowercase() == selectedKit.toString().lowercase()
                                        }
                            }
                        }?.firstOrNull()?.displayName?.let { conflict ->
                            return@of player.sendMessage("§cO kit ${kit.displayName} é incompatível com $conflict.")
                        }

                        arenaPlayer.setSelectedKit(kitNumber, kit)
                        it.whoClicked.closeInventory()
                        player.sendPlayerTitle("§a${kit.displayName}", "§eSelecionado!", 1)
                    }
                }

                contents.set(5, 4, ClickableItem.empty(
                    ItemBuilder()
                        .type(arenaPlayer.getSelectedKit(kitNumber)?.takeIf { it != ArenaKit.NONE }?.icon?.type ?: Material.ITEM_FRAME)
                        .displayName("§eKit Selecionado §8- §a${arenaPlayer.getSelectedKit(kitNumber)?.takeIf { it != ArenaKit.NONE }?.displayName ?: "§cNenhum"}")
                        .data(arenaPlayer.getSelectedKit(kitNumber)?.takeIf { it != ArenaKit.NONE }?.icon?.data?.data?.toInt() ?: 0)
                        .itemFlags(*ItemFlag.values())
                        .toItem
                ))

                val pagination = contents.pagination().apply {
                    setItems(*items.toTypedArray())
                    setItemsPerPage(21)
                    addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1)
                        .blacklist(2, 0).blacklist(3, 0).blacklist(1, 8)
                        .blacklist(2, 8).blacklist(3, 8))
                }

                if (!pagination.isFirst) {
                    contents.set(5, 0, ClickableItem.of(
                        ItemBuilder()
                            .type(Material.ARROW)
                            .displayName("§aPágina ${pagination.page}")
                            .toItem
                    ) {
                        inventory.provider(this@Provider).build()
                            .open(player, pagination.previous().page)
                    })
                }

                if (!pagination.isLast) {
                    contents.set(5, 8, ClickableItem.of(
                        ItemBuilder()
                            .type(Material.ARROW)
                            .displayName("§aPágina ${pagination.page.plus(1)}")
                            .toItem
                    ) {
                        inventory.provider(this@Provider).build()
                            .open(player, pagination.next().page)
                    })
                }
            }
        }
    }
}