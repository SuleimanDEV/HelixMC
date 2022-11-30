package club.helix.pvp.arena.kit.provider

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.kit.KitHandlerCooldown
import club.helix.pvp.arena.kotlin.player.allowedPvP
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEntityEvent
import java.util.*

class Monk: KitHandlerCooldown("monk-kit", 13) {

    override fun apply(player: Player): Unit = player.inventory.run {
        addItem(ItemBuilder()
            .type(Material.BLAZE_ROD)
            .displayName("§aEmbaralhar!")
            .identify("cancel-drop")
            .identify("kit", "monk")
            .toItem
        )
    }

    @EventHandler fun onInteractEntity(event: PlayerInteractEntityEvent) = event.takeIf {
        valid(it.player) && it.player.itemInHand != null && ItemBuilder.has(it.player.itemInHand, "kit", "monk")
                && (it.rightClicked as? Player)?.allowedPvP == true
    }?.apply {
        if (hasCooldownOrCreate(player)) return@apply sendCooldownMessage(player)

        val rightClicked = rightClicked as Player
        val hand = rightClicked.itemInHand
        val slot = Random().nextInt(35 + 1 - 9) + 9
        val slotItem = rightClicked.inventory.getItem(slot)

        rightClicked.itemInHand = slotItem
        rightClicked.inventory.setItem(slot, hand)
        rightClicked.inventory.heldItemSlot = 8

        rightClicked.sendMessage("§aSeu inventário foi embaralhado por ${player.name}.")
        player.sendMessage("§aVocê embaralhou o inventário de ${rightClicked.name}.")
    }
}