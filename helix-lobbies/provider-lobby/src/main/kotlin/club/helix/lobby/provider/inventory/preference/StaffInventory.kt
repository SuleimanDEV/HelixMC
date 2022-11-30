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

class StaffInventory(private val api: HelixComponents) {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("preference-staff")
            .title("Configurações da staff")
            .size(5, 9)
    }

    fun open(player: Player) = inventory.provider(Provider(api)).build().open(player)

    class Provider(private val api: HelixComponents): InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            val user = player.account
            val staff = user.preferences.staff
            val inventoryBuilder = inventory.provider(this).build()

            fun save() = api.userManager.redisController.save(user)

            contents.set(1, 1, ClickableItem.of(
                ItemBuilder()
                    .type(Material.BOOK_AND_QUILL)
                    .displayName("${if (staff.receivedStaffMessage) "§a" else "§c"}Mensagens da Staff")
                    .lore("§7Permitir receber mensagens", "§7da staff.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) { staff.receivedStaffMessage = !staff.receivedStaffMessage; save() } )

            contents.set(2, 1, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(if (staff.receivedStaffMessage) 10 else 8)
                    .displayName("${if (staff.receivedStaffMessage) "§a" else "§c"}Mensagens da Staff")
                    .lore("§7Clique para ${if (staff.receivedStaffMessage) "desativar" else "ativar"}.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) { staff.receivedStaffMessage = !staff.receivedStaffMessage; inventoryBuilder.open(player); save() } )

            contents.set(1, 3, ClickableItem.of(
                ItemBuilder()
                    .type(Material.TRIPWIRE_HOOK)
                    .displayName("${if (staff.notifyReports) "§a" else "§c"}Notificar Reportes")
                    .lore("§7Permitir receber denúncias", "§7dos jogadores.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) { staff.notifyReports = !staff.notifyReports; save() } )

            contents.set(2, 3, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(if (staff.notifyReports) 10 else 8)
                    .displayName("${if (staff.notifyReports) "§a" else "§c"}Notificar Reportes")
                    .lore("§7Clique para ${if (staff.notifyReports) "desativar" else "ativar"}.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) { staff.notifyReports = !staff.notifyReports; inventoryBuilder.open(player); save() } )

            contents.set(4, 4, ClickableItem.of(
                ItemBuilder()
                    .type(Material.ARROW)
                    .displayName("§aVoltar")
                    .toItem
            ) { PreferencesInventory(api).open(player) } )
        }
    }
}