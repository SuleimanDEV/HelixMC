package club.helix.event.specialitem

import club.helix.bukkit.builder.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class SpecialItem(
    val itemStack: ItemStack
) {

    LOCATOR(ItemBuilder()
        .type(Material.COMPASS)
        .displayName("§aLocalizador")
        .identify("special-item", "locator")
        .toItem
    );

    companion object {
        fun get(name: String) = values().firstOrNull { it.toString().lowercase() == name.lowercase() }
    }
}