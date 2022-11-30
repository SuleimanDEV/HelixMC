package club.helix.pvp.arena.kit.provider

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.kit.KitHandler
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.inventory.ItemFlag

class Fisherman: KitHandler() {

    override fun apply(player: Player): Unit = player.inventory.run {
        addItem(ItemBuilder()
            .type(Material.FISHING_ROD)
            .displayName("§aFisgar!")
            .identify("cancel-drop")
            .unbreakable()
            .itemFlags(ItemFlag.HIDE_UNBREAKABLE)
            .identify("kit", "fisherman")
            .toItem
        )
    }

    @EventHandler fun onFish(event: PlayerFishEvent) = event.takeIf {
        valid(it.player) && ItemBuilder.has(it.player.itemInHand, "kit", "fisherman")
                && it.state == PlayerFishEvent.State.CAUGHT_ENTITY && it.caught is Player
    }?.apply { caught.teleport(player) }
}