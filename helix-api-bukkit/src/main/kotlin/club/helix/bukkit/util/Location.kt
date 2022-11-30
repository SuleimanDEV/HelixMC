package club.helix.bukkit.util

class Location(
    var x: Double,
    var y: Double,
    var z: Double,
    var yaw: Float = 0f,
    var pitch: Float = 0f
) {
    fun clone() = Location(x, y, z, yaw, pitch)
}