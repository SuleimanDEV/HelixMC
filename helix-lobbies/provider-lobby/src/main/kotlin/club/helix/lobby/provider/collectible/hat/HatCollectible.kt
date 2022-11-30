package club.helix.lobby.provider.collectible.hat

import club.helix.bukkit.builder.ItemBuilder
import club.helix.lobby.provider.collectible.Collectible
import club.helix.lobby.provider.collectible.CollectibleCategory
import org.bukkit.entity.Player

open class HatCollectible(
    id: String,
    name: String,
    private val url: String,
    createdAt: Long
): Collectible(
    id,
    name,
    CollectibleCategory.HAT,
    ItemBuilder().customSkull(url).toItem,
    createdAt
) {

    override fun onEnable(player: Player) = player.inventory.run {
        helmet = ItemBuilder().customSkull(url)
            .displayName("§a${this@HatCollectible.name}")
            .identify("cancel-drop")
            .identify("cancel-click")
            .toItem
    }

    override fun onDisable(player: Player) = player.inventory.run {
        helmet = null
    }
}