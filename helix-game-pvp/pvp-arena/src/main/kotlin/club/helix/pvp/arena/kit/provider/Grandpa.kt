package club.helix.pvp.arena.kit.provider

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.kit.KitHandler
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

class Grandpa: KitHandler() {
    override fun apply(player: Player): Unit = player.inventory.run {
        addItem(ItemBuilder()
            .type(Material.STICK)
            .displayName("§aGrandpa")
            .enchantment(Enchantment.KNOCKBACK, 2)
            .identify("cancel-drop")
            .toItem
        )
    }
}