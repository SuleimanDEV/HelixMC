package club.helix.lobby.provider.inventory

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.connect
import club.helix.components.server.HelixServer
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.inv.content.SlotIterator
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag

class ServersInventory {
    companion object {
        enum class DisplayServer(
            val displayName: String,
            val target: HelixServer,
            val category: HelixServer.Category,
            val icon: Material,
            val description: Array<String>,
            val glow: Boolean = false,
        ) {
            DUELS("§aDuels (1v1/Gladiator)", HelixServer.DUELS, HelixServer.Category.LOBBY, Material.DIAMOND_SWORD, arrayOf(
                "§e{online} jogando agora."
            ), true),

            PVP("§aPvP", HelixServer.PVP, HelixServer.Category.LOBBY, Material.IRON_CHESTPLATE, arrayOf(
                "§e{online} jogando agora."
            )),

            HARDCOREGAMES("§aHG §c§lEM BREVE!", HelixServer.HG, HelixServer.Category.LOBBY, Material.MUSHROOM_SOUP, arrayOf(
                "§cEm breve disponível para todos."
            ));
        }

        private val inventory = HelixInventory.builder()
            .id("servers")
            .title("Selecione um jogo")
            .size(3, 9)
            .provider(Provider())
            .build()

        fun open(player: Player): Inventory = inventory.open(player)

        class Provider: InventoryProvider {
            override fun init(player: Player, contents: InventoryContents) {
                val items = mutableListOf<ClickableItem>()

                DisplayServer.values().forEach { displayServer ->
                    val itemBuilder = ItemBuilder()
                        .type(displayServer.icon)
                        .displayName(displayServer.displayName)
                        .lore(*displayServer.description.map { it.replace("{online}", displayServer.target.onlinePlayers.size.toString()) }.toTypedArray())
                        .itemFlags(*ItemFlag.values())
                    if (displayServer.glow) {
                        itemBuilder.enchantment(Enchantment.DAMAGE_UNDEAD, 1)
                    }

                    items.add(ClickableItem.of(itemBuilder.toItem) {
                        it.isCancelled = true

                        val availableServer = displayServer.target.findAvailable(displayServer.category)
                            ?: return@of player.sendMessage("§cNão há uma sala de ${displayServer.target.displayName} disponível.")

                        if (availableServer.type == HelixBukkit.instance.currentServer.type)
                            return@of player.sendMessage("§cVocê já está em um lobby de ${availableServer.type}.")

                        player.connect(availableServer, true)
                    })
                }

                contents.pagination().apply {
                    setItems(*items.toTypedArray())
                    setItemsPerPage(7)
                    addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1))
                }
            }
        }
    }
}