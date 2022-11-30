package club.helix.pvp.arena.handle

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.teleport
import club.helix.bukkit.util.Location
import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.kotlin.player.arenaPlayer
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player

class ServerSpawnHandle(private val plugin: PvPArena) {
    val spawnLocation = Location(39.35943778890438, 210.0, -15.523381091198791, 90.09671f, -0.45013496f)

    fun send(player: Player) = player.apply {
        arenaPlayer.pvp = false
        gameMode = GameMode.ADVENTURE
        maxHealth = 20.0
        health = maxHealth
        foodLevel = 20
        level = 0; exp = 0f
        inventory.clear()
        inventory.armorContents = null
        allowFlight = false
        isFlying = allowFlight
        inventory.heldItemSlot = 0
        activePotionEffects.forEach { removePotionEffect(it.type) }
        plugin.combatLog.remove(name)
        teleport(spawnLocation)

        inventory.setItem(0, ItemBuilder()
            .type(Material.CHEST)
            .displayName("§aSelecionar kit 1")
            .identify("cancel-click")
            .identify("cancel-drop")
            .identify("select-kit", "1")
            .toItem
        )
        inventory.setItem(1, ItemBuilder()
            .type(Material.CHEST)
            .displayName("§aSelecionar kit 2")
            .identify("cancel-click")
            .identify("cancel-drop")
            .identify("select-kit", "2")
            .toItem
        )
        inventory.setItem(2, ItemBuilder()
            .type(Material.EMERALD)
            .displayName("§aLoja de Kits")
            .identify("cancel-click")
            .identify("cancel-drop")
            .identify("spawn-item", "buy-kits")
            .toItem
        )
        inventory.setItem(8, ItemBuilder()
            .type(Material.BED)
            .displayName("§cVoltar ao Lobby")
            .identify("cancel-click")
            .identify("cancel-drop")
            .identify("spawn-item", "back-to-lobby")
            .toItem
        )
    }
}