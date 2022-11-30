package club.helix.lobby.provider.inventory.preference

import club.helix.components.HelixComponents
import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.lobby.provider.inventory.PreferencesInventory
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class PrivacyInventory(private val api: HelixComponents) {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("preference-privacy")
            .title("Configurações de privacidade")
            .size(5, 9)
    }

    fun open(player: Player) = inventory.provider(Provider(api)).build().open(player)

    class Provider(private val api: HelixComponents): InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            val user = player.account
            val privacy = user.preferences.privacy
            val inventoryBuilder = inventory.provider(this).build()

            fun save() = api.userManager.redisController.save(user)

            contents.set(1, 1, ClickableItem.of(
                ItemBuilder()
                    .type(Material.SIGN)
                    .displayName("${if (privacy.receivePrivateMessage) "§a" else "§c"}Mensagens privadas")
                    .lore("§7Permitir receber mensagens", "§7privadas.")
                    .toItem
            ) { privacy.receivePrivateMessage = !privacy.receivePrivateMessage; inventoryBuilder.open(player); save() } )

            contents.set(2, 1, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(if (privacy.receivePrivateMessage) 10 else 8)
                    .displayName("${if (privacy.receivePrivateMessage) "§a" else "§c"}Mensagens Privadas")
                    .lore("§7Clique para ${if (privacy.receivePrivateMessage) "desativar" else "ativar"}.")
                    .toItem
            ) { privacy.receivePrivateMessage = !privacy.receivePrivateMessage; inventoryBuilder.open(player); save() } )

            contents.set(1, 3, ClickableItem.of(
                ItemBuilder()
                    .type(Material.DOUBLE_PLANT)
                    .displayName("${if (privacy.receiveFriendshipRequest) "§a" else "§c"}Solicitações de Amizade")
                    .lore("§7Permitir receber solicitações", "§7de amizade.")
                    .toItem
            ) { privacy.receiveFriendshipRequest = !privacy.receiveFriendshipRequest; inventoryBuilder.open(player); save() } )

            contents.set(2, 3, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(if (privacy.receiveFriendshipRequest) 10 else 8)
                    .displayName("${if (privacy.receiveFriendshipRequest) "§a" else "§c"}Solicitações de Amizade")
                    .lore("§7Clique para ${if (privacy.receiveFriendshipRequest) "desativar" else "ativar"}.")
                    .toItem
            ) { privacy.receiveFriendshipRequest = !privacy.receiveFriendshipRequest; inventoryBuilder.open(player); save() } )

            contents.set(1, 5, ClickableItem.of(
                ItemBuilder()
                    .type(Material.IRON_SWORD)
                    .displayName("${if (privacy.receiveDuelInvitations) "§a" else "§c"}Convites de Duelo")
                    .lore("§7Qualquer um pode desafiar", "§7você para um duelo.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) { privacy.receiveDuelInvitations = !privacy.receiveDuelInvitations; inventoryBuilder.open(player); save() } )

            contents.set(2, 5, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(if (privacy.receiveDuelInvitations) 10 else 8)
                    .displayName("${if (privacy.receiveDuelInvitations) "§a" else "§c"}Convites de Duelo")
                    .lore("§7Clique para ${if (privacy.receiveDuelInvitations) "desativar" else "ativar"}.")
                    .toItem
            ) { privacy.receiveDuelInvitations = !privacy.receiveDuelInvitations; inventoryBuilder.open(player); save() } )

            contents.set(1, 7, ClickableItem.of(
                ItemBuilder()
                    .type(Material.PAINTING)
                    .displayName("${if (privacy.receiveClanInvitations) "§a" else "§c"}Convites de Clan")
                    .lore("§7Qualquer um pode convidar", "§7você para um clan.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) { privacy.receiveClanInvitations = !privacy.receiveClanInvitations; inventoryBuilder.open(player); save() } )

            contents.set(2, 7, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(if (privacy.receiveClanInvitations) 10 else 8)
                    .displayName("${if (privacy.receiveClanInvitations) "§a" else "§c"}Convites de Clan")
                    .lore("§7Clique para ${if (privacy.receiveClanInvitations) "desativar" else "ativar"}.")
                    .toItem
            ) { privacy.receiveClanInvitations = !privacy.receiveClanInvitations; inventoryBuilder.open(player); save() } )

            contents.set(4, 4, ClickableItem.of(
                ItemBuilder()
                    .type(Material.ARROW)
                    .displayName("§aVoltar")
                    .toItem
            ) { PreferencesInventory(api).open(player) } )
        }
    }
}