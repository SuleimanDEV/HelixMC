package club.helix.pvp.lava.util

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.teleport
import club.helix.bukkit.util.Location
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player

class ServerSpawn {

    private val spawnLocation = Location(0.48620227440219665, 65.0, 0.44598275950987054, 179.69896f, -1.6500012f)

    fun send(player: Player) = player.apply {
        gameMode = GameMode.ADVENTURE
        allowFlight = false
        isFlying = allowFlight
        maxHealth = 20.0
        health = maxHealth
        foodLevel = 20
        level = 0; exp = 0f
        inventory.clear()
        inventory.armorContents = null
        inventory.heldItemSlot = 0
        fireTicks = 0
        activePotionEffects.forEach { removePotionEffect(it.type) }
        teleport(spawnLocation)

        inventory.apply {
            setItem(13, ItemBuilder().type(Material.BOWL).amount(64).toItem)
            setItem(14, ItemBuilder().type(Material.RED_MUSHROOM).amount(64).toItem)
            setItem(15, ItemBuilder().type(Material.BROWN_MUSHROOM).amount(64).toItem)

            for (i in 0..35) {
                addItem(ItemBuilder().type(Material.MUSHROOM_SOUP).toItem)
            }
        }
    }
}