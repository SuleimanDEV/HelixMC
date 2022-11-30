package club.helix.pvp.arena.kit.provider

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.kit.KitHandler
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class Archer: KitHandler() {

    override fun apply(player: Player): Unit = player.run {
        inventory.addItem(
            ItemBuilder()
                .type(Material.BOW)
                .displayName("§aArco")
                .identify("cancel-drop")
                .unbreakable()
                .itemFlags(ItemFlag.HIDE_UNBREAKABLE)
                .toItem,
            ItemBuilder()
                .type(Material.ARROW)
                .amount(25)
                .displayName("§eFlechas")
                .identify("cancel-drop")
                .toItem
        )
    }
}