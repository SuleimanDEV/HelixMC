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
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

class ReportsInventory(
    private val components: HelixComponents,
    private val reports: List<HelixReport>
) {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("reports")
            .title("Denúncias")
            .size(6, 9)
        const val DISPLAY_REASONS_SIZE = 5
    }

    fun open(player: Player) = inventory.provider(Provider(components, reports))
        .build().open(player)

    private class Provider(
        private val components: HelixComponents,
        private val reports: List<HelixReport>
    ): InventoryProvider {

        override fun init(player: Player, contents: InventoryContents) {

            val items = reports.sortedBy { !it.isNew() }.map { report ->
                val size = report.getVictims().flatMap { it.value }.size
                val accusedUser = components.userManager.load(report.accused)
                val reasons = report.getVictims().flatMap { it.value }

                val itemBuilder = ItemBuilder()
                    .skull(report.accused)
                    .displayName("§7${report.accused}${if (report.isNew()) " §c§lNOVO!" else ""} ")
                    .lore(
                        "§8Denuciado ${size}x.",
                        "",
                        "§7Motivos §8($size)§7:",
                        *reasons.take(DISPLAY_REASONS_SIZE).map {
                            "§8- §7${it.takeIf { it.length <= 35 } ?: "${it.substring(0, 33)}..."}"
                        }.toMutableList().apply {
                            val otherReasons = reasons.size - DISPLAY_REASONS_SIZE
                            if (otherReasons > 0) add("§8+$otherReasons ${if (otherReasons > 1) "motivos" else "motivo"}...")
                        }.toTypedArray(),
                        "",
                        "§7Prioridade: ${report.priority.displayName}",
                        "§7Expira em: §c${HelixTimeFormat.format(report.remaingTime, true)}",
                        "§7Enviado há: §3${HelixTimeFormat.format(System.currentTimeMillis() - report.createdAt)}",
                        "",
                        "§aClique ESQUERDO para ver mais.",
                        "§aClique DIREITO para monitorar.",
                    )

                ClickableItem.of(itemBuilder.toItem) {
                    if (report.hasExpired()) {
                        player.sendMessage("§cEsta denúncia expirou.")
                        return@of player.closeInventory()
                    }

                    if (it.isRightClick) {
                        val recentReasons = report.getVictims().flatMap { map -> map.value }
                            .take(5)

                        player.closeInventory()
                        player.sendMessage(arrayOf(
                            "§aAnalisando o infrator ${report.accused}:",
                            "§7Status: ${HelixServer.getPlayerServer(report.accused)?.run {
                                "§aOnline §e($displayName)"
                            } ?: "§cOffline"}",
                            "§7Prioridade: ${report.priority.displayName}",
                            "§7Ultimo login: §b${accusedUser?.login?.lastLogin?.run {
                                HelixTimeFormat.format(System.currentTimeMillis() - this)
                            } ?: "§c{error-404}"}",
                            "",
                            "§7Infrações recentes:",
                            *recentReasons.map { map ->
                                "§8- §7${map.takeIf { map.length <= 25 } ?: "${map.substring(0, 22)}..."}"
                            }.toTypedArray(),
                            ""
                        ))
                        player.spigot().sendMessage(TextComponent(
                            "${ChatColor.AQUA}${ChatColor.BOLD}CLIQUE AQUI ${ChatColor.YELLOW}para se teleportar."
                        ).apply {
                            clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/goto ${report.accused}")
                            hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("§7/goto ${report.accused}").create())
                        })
                    }else {
                        IndividualReportInventory(components, report, reports).open(player)
                    }
                }
            }

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
                        .displayName("§aPagina ${pagination.page}")
                        .toItem
                ) { inventory.provider(this).build().open(player, pagination.previous().page) } )
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