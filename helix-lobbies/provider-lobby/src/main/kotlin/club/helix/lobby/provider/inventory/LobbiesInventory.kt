package club.helix.lobby.provider.inventory

import club.helix.components.server.HelixServer
import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.connect
import club.helix.inv.ClickableItem
import club.helix.inv.HelixInventory
import club.helix.inv.content.InventoryContents
import club.helix.inv.content.InventoryProvider
import club.helix.inv.content.SlotIterator
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.entity.Player

class LobbiesInventory(target: HelixServer) {
    private val inventory = HelixInventory.builder()
        .id("lobbies")
        .title("Selecione um lobby")
        .size(3, 9)
        .provider(Provider(target))
        .build()

    fun open(player: Player) = inventory.open(player)

    class Provider(private val target: HelixServer): InventoryProvider {
        override fun init(player: Player, contents: InventoryContents) {
            val items = mutableListOf<ClickableItem>()
            val pagination = contents.pagination()

            target.providers(HelixServer.Category.LOBBY).forEach { server ->
                var data = 15

                if (server.onlinePlayers.contains(player.name))
                    data = 5
                else if (!server.online)
                    data = 14

                items.add(
                    ClickableItem.of(
                    ItemBuilder()
                        .type(Material.STAINED_GLASS_PANE)
                        .data(data)
                        .displayName("${if (server.online) "§a" else "§c"}${server.displayName}")
                        .lore("§fJogadores: ${if (server.online) "§a" else "§c"}${server.onlinePlayers.size}/${server.maxPlayers}")
                        .toItem
                ) {
                        it.isCancelled = true

                        if (!server.online || server.onlinePlayers.contains(player.name))
                            return@of

                        player.spigot().sendMessage(TextComponent("§aConectando...").apply {
                            hoverEvent = HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                ComponentBuilder("§7Dê: §f${HelixBukkit.instance.currentServer.serverName}\n" +
                                        "§7Para: §f${server.serverName}").create())
                        })
                        player.connect(server)
                })
            }

            pagination.setItems(*items.toTypedArray())
            pagination.setItemsPerPage(7)
            pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1))
        }
    }
}