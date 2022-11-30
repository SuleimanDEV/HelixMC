package club.helix.bukkit.kotlin.player

import org.bukkit.Location
import org.bukkit.entity.Player

fun Player.teleport(x: Double, y: Double, z: Double, yaw: Float = location.yaw, pitch: Float = location.pitch) = let { it.teleport(
    Location(it.world, x, y, z, yaw, pitch)
) }

fun Player.teleport(location: club.helix.bukkit.util.Location) =
    teleport(location.x, location.y, location.z, location.yaw, location.pitch)