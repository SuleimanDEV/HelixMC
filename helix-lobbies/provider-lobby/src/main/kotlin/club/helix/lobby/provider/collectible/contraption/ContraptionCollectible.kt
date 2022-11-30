package club.helix.lobby.provider.collectible.contraption

import club.helix.components.util.HelixTimeData
import club.helix.lobby.provider.collectible.Collectible
import club.helix.lobby.provider.collectible.CollectibleCategory
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.TimeUnit

abstract class ContraptionCollectible(
    id: String,
    name: String,
    private val cooldown: Long,
    icon: ItemStack,
    createdAt: Long
): Collectible(
    id,
    name,
    CollectibleCategory.CONTRAPTION,
    icon,
    createdAt
) {
    abstract val item: ItemStack

    override fun onEnable(player: Player) {
        player.inventory.addItem(item)
    }

    override fun onDisable(player: Player) {
        player.inventory.remove(item)
    }

    fun interactHandle(player: Player) {
        if (HelixTimeData.getOrCreate(player.name, id, cooldown, TimeUnit.SECONDS)) {
            return player.sendMessage("§cAguarde ${HelixTimeData.getTimeFormatted(player.name, id)} para utilizar a engenhoca novamente.")
        }
        onInteract(player)
    }

    abstract fun onInteract(player: Player)

}