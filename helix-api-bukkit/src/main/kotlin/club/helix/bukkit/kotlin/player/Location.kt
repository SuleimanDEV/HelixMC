package club.helix.bukkit.kotlin.player

import org.bukkit.Location
import org.bukkit.entity.Player

fun Player.location(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) =
    Location(world, x, y, z, yaw, pitch)