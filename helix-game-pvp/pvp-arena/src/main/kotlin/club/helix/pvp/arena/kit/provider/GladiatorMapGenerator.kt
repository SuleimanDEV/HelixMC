package club.helix.pvp.arena.kit.provider

import club.helix.bukkit.HelixBukkit
import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.Vector
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import com.sk89q.worldedit.schematic.MCEditSchematicFormat
import org.bukkit.Location
import java.io.File
import kotlin.math.floor

class GladiatorMapGenerator(
    private val base: Location,
    private val worldEditPlugin: WorldEditPlugin,
    schematic: File,
    maxX: Int = 9999999,
    maxZ: Int = 9999999,
) {
    lateinit var session: EditSession

    private val x = floor(Math.random() * maxX)
    private val z = floor(Math.random() * maxZ)
    val center = Location(HelixBukkit.instance.world, x, 80.0, z, -179.7f, -2.9f)
    private val clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic)
        ?: throw NullPointerException("invalid schematic/clipboard")

    fun load() {
        try {
            session = worldEditPlugin.worldEdit.editSessionFactory
                .getEditSession(BukkitWorld(base.world), worldEditPlugin.worldEdit.configuration.maxChangeLimit)
            clipboard.paste(session, Vector(center.x, center.y, center.z), false)
        }catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun unload() {
        try {
            session.undo(session)
        }catch (error: Exception) {
            error.printStackTrace()
        }
    }

}