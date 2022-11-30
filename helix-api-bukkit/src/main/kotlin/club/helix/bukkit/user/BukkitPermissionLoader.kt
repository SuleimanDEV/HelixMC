package club.helix.bukkit.user

import club.helix.bukkit.HelixBukkit
import club.helix.components.account.HelixRank
import club.helix.components.account.permission.PermissionLoader
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class BukkitPermissionLoader(apiBukkit: HelixBukkit): PermissionLoader {

    private val file = File(apiBukkit.dataFolder, "permissions.yml")

    init {
        apiBukkit.saveResource(file.name, true)
    }

    private fun isValidFile() = file.exists() && file.isFile

    override fun load(rank: HelixRank) = ArrayList<String>().apply {
        try {
            if (!isValidFile()) {
                throw NullPointerException("invalid permissions file")
            }
            val yml = YamlConfiguration.loadConfiguration(file)

            val configPath = "ranks.$rank"
            val permissionsMap = mutableListOf(rank.toString())
            yml.getStringList("$configPath.aliases")?.let { permissionsMap.addAll(it) }

            permissionsMap.forEach {
                val permissions = yml.getStringList("ranks.$it.permissions") ?: return@apply
                addAll(permissions)
            }
        }catch (error: Exception) {
            error.printStackTrace()
        }
    }

    override fun unloadFile() {
        try {
            if (!isValidFile()) {
                throw NullPointerException("invalid permissions file")
            }
            file.delete()
        }catch (error: Exception) {
            error.printStackTrace()
        }
    }
}