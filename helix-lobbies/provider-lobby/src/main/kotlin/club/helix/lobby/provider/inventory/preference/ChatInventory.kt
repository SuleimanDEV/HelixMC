package club.helix.lobby.provider.inventory.preference

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.components.HelixComponents
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.lobby.provider.inventory.PreferencesInventory
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class ChatInventory(private val components: HelixComponents) {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("preference-chat")
            .title("Configurações do chat")
            .size(5, 9)
    }

    fun open(player: Player) = inventory.provider(Provider(components)).build().open(player)

    class Provider(private val components: HelixComponents): InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            val user = player.account
            val chat = user.preferences.chat
            val inventoryBuilder = inventory.provider(this).build()


            fun save() = components.userManager.redisController.save(user)

            contents.set(1, 1, ClickableItem.of(
                ItemBuilder()
                    .type(Material.WRITTEN_BOOK)
                    .displayName("${if (chat.receiveChatMessage) "§a" else "§c"}Bate-papo")
                    .lore("§7Mostrar o bate-papo", "§7geral.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) { chat.receiveChatMessage = !chat.receiveChatMessage; inventoryBuilder.open(player); save() })

            contents.set(2, 1, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(if (chat.receiveChatMessage) 10 else 8)
                    .displayName("${if (chat.receiveChatMessage) "§a" else "§c"}Bate-papo")
                    .lore("§7Clique para ${if (chat.receiveChatMessage) "desativar" else "ativar"}.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) { chat.receiveChatMessage = !chat.receiveChatMessage; inventoryBuilder.open(player); save() })

            contents.set(1, 3, ClickableItem.of(
                ItemBuilder()
                    .type(Material.SIGN)
                    .displayName("${if (chat.receiveClanMessage) "§a" else "§c"}Mensagens do Clan")
                    .lore("§7Receber mensagens do clan.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) { chat.receiveClanMessage = !chat.receiveClanMessage; inventoryBuilder.open(player); save() })

            contents.set(2, 3, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(if (chat.receiveClanMessage) 10 else 8)
                    .displayName("${if (chat.receiveClanMessage) "§a" else "§c"}Mensagens do Clan")
                    .lore("§7Clique para ${if (chat.receiveClanMessage) "desativar" else "ativar"}.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) { chat.receiveClanMessage = !chat.receiveClanMessage; inventoryBuilder.open(player); save() })

            contents.set(4, 4, ClickableItem.of(
                ItemBuilder()
                    .type(Material.ARROW)
                    .displayName("§aVoltar")
                    .toItem
            ) { PreferencesInventory(components).open(player) } )
        }
    }
}