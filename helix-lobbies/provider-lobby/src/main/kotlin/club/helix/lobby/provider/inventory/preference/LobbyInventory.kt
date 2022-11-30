package club.helix.lobby.provider.inventory.preference

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.components.HelixComponents
import club.helix.components.account.HelixRank
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.lobby.provider.inventory.PreferencesInventory
import club.helix.lobby.provider.util.PlayerVisible
import club.helix.lobby.provider.util.SpawnItems
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class LobbyInventory(private val api: HelixComponents) {
    companion object {
        private val inventory = HelixInventory.builder()
            .id("preference-lobby")
            .title("Configurações do lobby")
            .size(5, 9)
    }

    fun open(player: Player) = inventory.provider(Provider(api)).build().open(player)

    class Provider(private val api: HelixComponents): InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            val user = player.account
            val lobby = user.preferences.lobby
            val inventoryBuilder = inventory.provider(this).build()

            fun save() = api.userManager.redisController.save(user)

            contents.set(1, 1, ClickableItem.of(
                ItemBuilder()
                    .type(Material.COMMAND)
                    .displayName("${if (lobby.gameProtection) "§a" else "§c"}Proteção no /lobby")
                    .lore("§7Requisitar o comando /lobby 2", "§7vezes quando estiver em jogo.")
                    .toItem
            ) { lobby.gameProtection = !lobby.gameProtection; inventoryBuilder.open(player); save() })

            contents.set(2, 1, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(if (lobby.gameProtection) 10 else 8)
                    .displayName("${if (lobby.gameProtection) "§a" else "§c"}Proteção no /lobby")
                    .lore("§7Clique para ${if (lobby.gameProtection) "desativar" else "ativar"}.")
                    .toItem
            ) { lobby.gameProtection = !lobby.gameProtection; inventoryBuilder.open(player); save() })

            contents.set(1, 3, ClickableItem.of(
                ItemBuilder()
                    .type(Material.WATCH)
                    .displayName("${if (lobby.playersVisible) "§a" else "§c"}Visibilidade dos Jogadores")
                    .lore("§7Ver outros jogadores nos lobbies.")
                    .toItem
            ) {
                lobby.playersVisible = !lobby.playersVisible
                inventoryBuilder.open(player)
                SpawnItems.set(player)
                PlayerVisible.handle(player)
                save()
            })

            contents.set(2, 3, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(if (lobby.playersVisible) 10 else 8)
                    .displayName("${if (lobby.playersVisible) "§a" else "§c"}Visibilidade dos Jogadores")
                    .lore("§7Clique para ${if (lobby.playersVisible) "desativar" else "ativar"}.")
                    .toItem
            ) {
                lobby.playersVisible = !lobby.playersVisible
                inventoryBuilder.open(player)
                SpawnItems.set(player)
                PlayerVisible.handle(player)
                save()
            })

            contents.set(1, 5, ClickableItem.of(
                ItemBuilder()
                    .type(Material.CARPET)
                    .data(14)
                    .displayName("${if (lobby.sendJoinMessage) "§a" else "§c"}Mensagem de Entrada")
                    .lore("§7Enviar mensagem de entrada", "§7ao entrar em um lobby.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) {
                if (HelixRank.vip(user.mainRankLife.rank)) {
                    lobby.sendJoinMessage = !lobby.sendJoinMessage
                    inventoryBuilder.open(player)
                    save()
                }
            })

            contents.set(2, 5, ClickableItem.of(
                ItemBuilder()
                    .type(Material.INK_SACK)
                    .data(if (lobby.sendJoinMessage) 10 else 8)
                    .displayName("${if (lobby.sendJoinMessage) "§a" else "§c"}Mensagens de Entrada")
                    .lore("§7Clique para ${if (lobby.sendJoinMessage) "desativar" else "ativar"}.")
                    .itemFlags(*ItemFlag.values())
                    .toItem
            ) {
                if (HelixRank.vip(user.mainRankLife.rank)) {
                    lobby.sendJoinMessage = !lobby.sendJoinMessage
                    inventoryBuilder.open(player)
                    save()
                }
            })

            contents.set(4, 4, ClickableItem.of(
                ItemBuilder()
                    .type(Material.ARROW)
                    .displayName("§aLobby")
                    .toItem
            ) { PreferencesInventory(api).open(player) } )
        }
    }
}