package club.helix.duels.uhc

import club.helix.bukkit.HelixBukkit
import club.helix.duels.api.game.DuelsGame
import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.Vector
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import com.sk89q.worldedit.schematic.MCEditSchematicFormat
import org.bukkit.Bukkit
import org.bukkit.Location
import kotlin.math.floor

class UHCMapGenerator(
    val game: DuelsGame,
    maxX: Int = 9999999,
    maxZ: Int = 9999999,
) {
    private val plugin: UHCPlugin = UHCPlugin.instance

    private val x = floor(Math.random() * maxX)
    private val z = floor(Math.random() * maxZ)
    val location = Location(HelixBukkit.instance.world, x, 55.0, z, -179.7f, -2.9f)

    private val worldEditPlugin = Bukkit.getPluginManager().getPlugin("WorldEdit")
            as? WorldEditPlugin ?: throw NullPointerException("invalid world edit plugin")
    lateinit var session: EditSession

    private val arenaSchematic = UHCPlugin.instance.arenaSchematic
    private val clipboard = MCEditSchematicFormat.getFormat(arenaSchematic).load(arenaSchematic)
        ?: throw NullPointerException("invalid clipboard")

    fun load() {
        try {
            session = worldEditPlugin.worldEdit.editSessionFactory
                .getEditSession(BukkitWorld(location.world), worldEditPlugin.worldEdit.configuration.maxChangeLimit)
            clipboard.paste(session, Vector(location.x, location.y, location.z), false)
        }catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun unload() = Bukkit.getScheduler().runTaskAsynchronously(plugin) {
        session.undo(session)
    }
}