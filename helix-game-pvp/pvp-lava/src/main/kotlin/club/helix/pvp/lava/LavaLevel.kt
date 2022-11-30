package club.helix.pvp.lava

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.util.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class LavaLevel(
    val displayName: String,
    val icon: ItemStack,
    val location: Location
) {
    ONE(
        "§a§lLevel 1",
        ItemBuilder().type(Material.STAINED_GLASS_PANE).data(5).toItem,
        Location(9.751126102004926, 64.0, -54.082743861263396, 178.92966f, 0.8480964f)
    ),
    TWO(
        "§e§lLevel 2",
        ItemBuilder().type(Material.STAINED_GLASS_PANE).data(4).toItem,
        Location(8.99875881172311, 64.0, 46.27169871907089, 359.5294f, 0.09809306f)
    ),
    THREE(
        "§6§lLevel 3",
        ItemBuilder().type(Material.STAINED_GLASS_PANE).data(1).toItem,
        Location(-31.991393044557633, 64.0, 0.48719669661952925, 89.82968f, 0.24809736f)
    ),
    FOUR(
        "§c§lLevel 4",
        ItemBuilder().type(Material.STAINED_GLASS_PANE).data(14).toItem,
        Location(31.860041316615845, 66.0, 0.6251033225038398, 269.38007f, 0.9981049f)
    );
}