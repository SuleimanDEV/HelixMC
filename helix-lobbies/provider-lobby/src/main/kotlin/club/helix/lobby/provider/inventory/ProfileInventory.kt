package club.helix.lobby.provider.inventory

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.components.HelixComponents
import club.helix.components.util.HelixTimeFormat
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import java.text.SimpleDateFormat

class ProfileInventory(private val components: HelixComponents) {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("profile")
            .title("Perfil")
            .size(5, 9)
    }

    fun open(player: Player) = inventory.provider(Provider(components)).build().open(player)

    class Provider(private val components: HelixComponents): InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            val user = player.account
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")

            contents.set(1, 4, ClickableItem.empty(
                ItemBuilder()
                    .skull(player.name)
                    .displayName("§a${player.name}")
                    .lore(
                        "§7Rank: ${user.mainRankLife.rank.coloredName} §e(${user.mainRankLife.formatTime()})",
                        "§7Tempo de jogo: ${HelixTimeFormat.format(user.onlineTime)}",
                        "§7Primeiro login: ${dateFormat.format(user.login.firstLogin)}",
                        "§7Ultimo login: ${dateFormat.format(user.login.lastLogin)}"
                    )
                    .identify("cancel-click")
                    .toItem
            ))

            contents.set(3, 2, ClickableItem.of(ItemBuilder()
                .type(Material.PAPER)
                .displayName("§aVer estatísticas")
                .lore(
                    "§7Veja suas estatísticas",
                    "§7de todos os jogos."
                )
                .identify("cancel-click")
                .toItem
            ) { player.performCommand("stats") } )

            contents.set(3, 3, ClickableItem.of(ItemBuilder()
                .type(Material.NAME_TAG)
                .displayName("§aMedalhas")
                .lore("§7Veja e selecione medalhas.")
                .identify("cancel-click")
                .toItem
            ) { player.performCommand("medal"); player.closeInventory() } )

            contents.set(3, 4, ClickableItem.of(ItemBuilder()
                .customSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjFkZDRmZTRhNDI5YWJkNjY1ZGZkYjNlMjEzMjFkNmVmYTZhNmI1ZTdiOTU2ZGI5YzVkNTljOWVmYWIyNSJ9fX0=\\")
                .displayName("§aSelecionar idioma")
                .lore("§7Alterar seu idioma.")
                .identify("cancel-click")
                .toItem
            ) { LanguageInventory(components).open(player) } )

            contents.set(3, 5, ClickableItem.of(ItemBuilder()
                .type(Material.DIODE)
                .displayName("§aPreferências")
                .lore("§7Alterar suas preferências.")
                .identify("cancel-click")
                .toItem
            ) { PreferencesInventory(components).open(player) } )

            contents.set(3, 6, ClickableItem.empty(ItemBuilder()
                .type(Material.ITEM_FRAME)
                .displayName("§aSua skin")
                .lore("§cTemporariamente desativado.")
                .identify("cancel-click")
                .toItem
            ))
        }
    }
}