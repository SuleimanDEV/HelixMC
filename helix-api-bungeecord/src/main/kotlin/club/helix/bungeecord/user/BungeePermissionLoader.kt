package club.helix.bungeecord.user

import club.helix.bungeecord.HelixBungee
import club.helix.components.account.HelixRank
import club.helix.components.account.permission.PermissionLoader
import com.google.common.io.ByteStreams
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.io.FileOutputStream

class BungeePermissionLoader(plugin: HelixBungee): PermissionLoader {

    private val file = File(plugin.dataFolder, "permissions.yml")

    init {
        plugin.dataFolder.takeIf { !it.exists() }?.mkdir()
        if (file.exists()) {
            unloadFile()
        }
        file.createNewFile()

        javaClass.classLoader.getResourceAsStream(file.name).use {
            val outputStream = FileOutputStream(file)
            ByteStreams.copy(it, outputStream)
        }
    }

    private fun isValidFile() = file.exists() && file.isFile

    override fun load(rank: HelixRank) = ArrayList<String>().apply {
        try {
            if (!isValidFile()) {
                throw NullPointerException("invalid permissions file")
            }

            val yml = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(file)
                ?: throw NullPointerException("invalid yaml config")

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