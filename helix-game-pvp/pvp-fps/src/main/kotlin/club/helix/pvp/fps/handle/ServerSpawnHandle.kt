package club.helix.pvp.fps.handle

import club.helix.bukkit.kotlin.player.teleport
import club.helix.bukkit.util.Location
import club.helix.pvp.fps.PvPFps
import club.helix.pvp.fps.kotlin.player.removePvP
import org.bukkit.GameMode
import org.bukkit.entity.Player

class ServerSpawnHandle(private val plugin: PvPFps) {

    val spawnLocation = Location(0.5056091502943081, 65.0, 0.5669238327101421, -180.0f, 0.7500091f)

    fun send(player: Player): Unit = player.run {
        level = 0
        exp = 0f
        maxHealth = 20.0
        health = 20.0
        foodLevel = 20
        inventory.clear()
        inventory.armorContents = null
        allowFlight = false
        isFlying = allowFlight
        gameMode = GameMode.ADVENTURE
        activePotionEffects.forEach { removePotionEffect(it.type) }
        plugin.combatLog.remove(name)
        teleport(spawnLocation)
        removePvP()
    }
}