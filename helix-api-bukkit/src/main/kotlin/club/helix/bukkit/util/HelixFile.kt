package club.helix.bukkit.util

import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class HelixFile(
    private val name: String,
    private val dataFolder: File
) {

    private val file = File(dataFolder, name)
    val yaml: YamlConfiguration = YamlConfiguration.loadConfiguration(file)

    init { save() }

    fun save() {
        try {
            yaml.save(file)
        }catch (error: Exception) {
            error.printStackTrace()
            Bukkit.getConsoleSender().sendMessage("§c§lAPI: §fOcorreu um erro ao salvar o arquivo §c$name")
        }
    }
}