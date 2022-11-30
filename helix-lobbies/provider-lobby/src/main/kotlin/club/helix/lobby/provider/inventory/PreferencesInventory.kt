package club.helix.lobby.provider.inventory

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.components.HelixComponents
import club.helix.components.account.HelixRank
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.lobby.provider.inventory.preference.ChatInventory
import club.helix.lobby.provider.inventory.preference.LobbyInventory
import club.helix.lobby.provider.inventory.preference.PrivacyInventory
import club.helix.lobby.provider.inventory.preference.StaffInventory
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag

class PreferencesInventory(private val components: HelixComponents) {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("preferences")
            .title("Preferências")
            .size(5, 9)
    }

    fun open(player: Player): Inventory = inventory.provider(Provider(components)).build().open(player)

    class Provider(private val components: HelixComponents): InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            val user = player.account
            val inventoryBuilder = inventory.provider(this).build()

            contents.set(1, 1, ClickableItem.of(
                ItemBuilder()
                    .type(Material.BED)
                    .displayName("${if (user.preferences.automaticPull) "§a" else "§c"}Puxar Automaticamente")
                    .lore("§7Enviar para uma nova partida", "§7no fim do jogo.")
                    .toItem
            ) {
                user.preferences.automaticPull = !user.preferences.automaticPull
                inventoryBuilder.open(player)
                components.userManager.saveAll(user)
            } )

            contents.set(2, 1, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(if (user.preferences.automaticPull) 10 else 8)
                    .displayName("${if (user.preferences.automaticPull) "§a" else "§c"}Puxar Automaticamente")
                    .lore("§7Clique para ${if (user.preferences.automaticPull) "desativar" else "ativar"}.")
                    .toItem
            ) {
                user.preferences.automaticPull = !user.preferences.automaticPull
                inventoryBuilder.open(player)
                components.userManager.saveAll(user)
            } )

            contents.set(1, 3, ClickableItem.of(
                ItemBuilder()
                    .type(Material.WRITTEN_BOOK)
                    .displayName("§aConfigurações do Chat")
                    .lore("§7Clique para configurar.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) { ChatInventory(components).open(player) } )

            contents.set(2, 3, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(14)
                    .displayName("§aConfigurações do Chat")
                    .lore("§7Clique para configurar.")
                    .toItem
            ) { ChatInventory(components).open(player) } )

            contents.set(1, 5, ClickableItem.of(
                ItemBuilder()
                    .type(Material.BARRIER)
                    .displayName("§aConfigurações de Privacidade")
                    .lore("§7Clique para configurar.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) { PrivacyInventory(components).open(player) } )

            contents.set(2, 5, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(14)
                    .displayName("§aConfigurações do Privacidade.")
                    .lore("§7Clique para configurar.")
                    .toItem
            ) { PrivacyInventory(components).open(player) } )

            contents.set(1, 7, ClickableItem.of(
                ItemBuilder()
                    .type(Material.NETHER_STAR)
                    .displayName("§aConfigurações do Lobby")
                    .lore("§7Clique para configurar.")
                    .toItem
            ) { LobbyInventory(components).open(player) } )

            contents.set(2, 7, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(14)
                    .displayName("§aConfigurações do Lobby")
                    .lore("§7Clique para configurar.")
                    .toItem
            ) { LobbyInventory(components).open(player) } )

            if (HelixRank.staff(user.mainRankLife.rank)) {
                contents.set(4, 3, ClickableItem.of(
                    ItemBuilder()
                        .type(Material.DARK_OAK_DOOR_ITEM)
                        .displayName("§aConfigurações da Staff")
                        .lore("§7Clique para configurar.")
                        .toItem
                ) { StaffInventory(components).open(player) } )
            }

            contents.set(4, 4, ClickableItem.of(
                ItemBuilder()
                    .type(Material.ARROW)
                    .displayName("§aVoltar")
                    .toItem
            ) { ProfileInventory(components).open(player) } )
        }
    }
}
