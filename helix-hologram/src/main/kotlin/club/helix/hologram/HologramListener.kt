package club.helix.hologram

import club.helix.hologram.Hologram.Companion.holograms
import club.helix.hologram.line.HologramLine
import club.helix.hologram.listener.UserQuitListener
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

class HologramListener(val plugin: JavaPlugin) {

    fun start() {
        PacketInteractListener(this).register()
        plugin.server.pluginManager.registerEvents(UserQuitListener(this), plugin)

        HologramUpdateTask().runTaskTimerAsynchronously(plugin, 0, 3 * 20L)
    }

    fun create(location: Location, vararg lines: HologramLine)
        = Hologram(location, *lines).apply { holograms.add(this) }
}