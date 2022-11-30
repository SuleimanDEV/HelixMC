package club.helix.bukkit.inventory.report

import club.helix.bukkit.builder.ItemBuilder
import club.helix.components.HelixComponents
import club.helix.components.account.HelixReport
import club.helix.components.server.HelixServer
import club.helix.components.util.HelixTimeFormat
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.inv.content.SlotIterator
import org.bukkit.Material
import org.bukkit.entity.Player

class IndividualReportInventory(
    private val components: HelixComponents,
    private val report: HelixReport,
    private val reports: List<HelixReport>
) {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("reports")
            .size(6, 9)

        const val DISPLAY_REASONS_SIZE = 15

        private fun handleTitle(report: HelixReport) =
            "Visualizando ${report.accused.takeIf { it.length <= 40 } ?: "${report.accused.substring(0, 37)}..."}"

    }

    fun open(player: Player) = inventory
        .title(handleTitle(report))
        .provider(Provider(report, reports, components))
        .build()
        .open(player)

    private class Provider(
        private val report: HelixReport,
        private val reports: List<HelixReport>,
        private val components: HelixComponents
    ): InventoryProvider {

        override fun init(player: Player, contents: InventoryContents) {
            val accusedUser = components.userManager.load(report.accused)

            contents.set(0, 4, ClickableItem.empty(
                ItemBuilder()
                    .skull(report.accused)
                    .displayName("§c${report.accused}")
                    .lore(
                        "§7Rank: ${accusedUser?.mainRankLife?.rank?.run { color + displayName } ?: "§c{error-404}"}",
                        "§7Status: ${HelixServer.getPlayerServer(report.accused)?.run { 
                            "§aOnline §e($displayName)"
                        } ?: "§cOffline"}",
                        "§7Ultimo login: §b${accusedUser?.login?.lastLogin?.run {
                            HelixTimeFormat.format(System.currentTimeMillis() - this)
                        } ?: "§c{error-404}"}"
                    )
                    .toItem
            ))

            var victimId = 1

            val items = report.getVictims().map { victim ->
                val reasons = victim.value

                ClickableItem.empty(
                    ItemBuilder()
                        .type(Material.PAPER)
                        .amount(victimId++)
                        .displayName("§7Enviado por: §e${victim.key}")
                        .lore(
                            *reasons.take(DISPLAY_REASONS_SIZE).map {
                                "§8- §7${it.takeIf { it.length <= 25 } ?: "${it.substring(0, 23)}..."}"
                            }.toMutableList().apply {
                                val otherReasons = reasons.size - DISPLAY_REASONS_SIZE
                                if (otherReasons > 0) add("§8+$otherReasons ${if (otherReasons > 1) "motivos" else "motivo"}...")
                            }.toTypedArray()
                        )
                        .toItem
                )
            }

            val pagination = contents.pagination().apply {
                setItems(*items.toTypedArray())
                setItemsPerPage(7 * 3)
                addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 2, 1)
                    .blacklist(2, 0).blacklist(3, 0).blacklist(1, 8)
                    .blacklist(2, 8).blacklist(3, 8))
            }

            contents.set(5, 4, ClickableItem.of(
                ItemBuilder()
                    .type(Material.ARROW)
                    .displayName("§aVoltar")
                    .toItem
            ) { ReportsInventory(components, reports).open(player) })

            if (!pagination.isFirst) {
                contents.set(2, 0, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aPagina ${pagination.page}")
                        .toItem
                ) {
                    inventory.title(handleTitle(report)).provider(this).build()
                        .open(player, pagination.previous().page)
                } )
            }

            if (!pagination.isLast) {
                contents.set(2, 8, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.ARROW)
                        .displayName("§aPagina ${pagination.page.plus(2)}")
                        .toItem
                ) {
                    inventory.title(handleTitle(report)).provider(this).build()
                        .open(player, pagination.next().page)
                } )
            }
        }
    }
}