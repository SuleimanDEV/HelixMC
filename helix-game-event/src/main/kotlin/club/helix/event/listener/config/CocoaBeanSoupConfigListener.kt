package club.helix.event.listener.config

import club.helix.event.EventPlugin
import club.helix.event.event.GameConfigChangeEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe

class CocoaBeanSoupConfigListener(private val plugin: EventPlugin): Listener {
    companion object {
        private val RECIPE = ShapelessRecipe(ItemStack(Material.MUSHROOM_SOUP)).apply {
            addIngredient(1, Material.INK_SACK, 3)
            addIngredient(1, Material.BOWL)
        }
    }

    @EventHandler fun onConfigChange(event: GameConfigChangeEvent<Boolean>) = event.takeIf {
        it.configName == "cocoa-bean-soup"
    }?.apply {
        if (!value) {
            val iterator = Bukkit.recipeIterator()

            while (iterator.hasNext()) {
                val recipe = iterator.next() as? ShapelessRecipe ?: continue

                if (recipe.ingredientList == RECIPE.ingredientList) {
                    iterator.remove()
                }
            }
        }else {
            Bukkit.addRecipe(RECIPE)
        }
    }
}