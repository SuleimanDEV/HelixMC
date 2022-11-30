package club.helix.lobby.provider.collectible

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.TimeUnit

abstract class Collectible(
    val id: String,
    val name: String,
    val category: CollectibleCategory,
    val icon: ItemStack,
    val createdAt: Long
) {
    abstract fun onEnable(player: Player)
    abstract fun onDisable(player: Player)

    fun isNew() = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - createdAt) <= 3
}