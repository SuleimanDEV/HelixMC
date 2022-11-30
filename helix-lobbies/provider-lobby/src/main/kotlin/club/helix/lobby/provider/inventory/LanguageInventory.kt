package club.helix.lobby.provider.inventory

import club.helix.bukkit.builder.ItemBuilder
import club.helix.components.HelixComponents
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player

class LanguageInventory(private val components: HelixComponents) {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("profile-language")
            .title("Idiomas disponíveis")
            .size(3, 9 )
    }

    fun open(player: Player) = inventory.provider(Provider(components)).build().open(player)

    class Provider(private val components: HelixComponents): InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            contents.set(1, 1, ClickableItem.empty(
                ItemBuilder()
                    .customSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWE0NjQ3NWQ1ZGNjODE1ZjZjNWYyODU5ZWRiYjEwNjExZjNlODYxYzBlYjE0ZjA4ODE2MWIzYzBjY2IyYjBkOSJ9fX0=\\")
                    .displayName("§aPortuguês (Brasil)")
                    .lore(
                        "§7Alterar seu idioma para",
                        "§7português do Brasil.",
                        "",
                        "§eIdioma selecionado."
                    )
                    .toItem
            ))

            contents.set(2, 4, ClickableItem.of(
                ItemBuilder()
                    .type(Material.ARROW)
                    .displayName("§aVoltar")
                    .toItem
            ) { ProfileInventory(components).open(player) } )
        }
    }
}