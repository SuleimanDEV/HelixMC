package club.helix.pvp.arena.recipe

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe

class CocoaBeanSoup() {
    fun register() = ShapelessRecipe(ItemStack(Material.MUSHROOM_SOUP)).apply {
        addIngredient(1, Material.INK_SACK, 3)
        addIngredient(1, Material.BOWL)
        Bukkit.addRecipe(this)
    }
}