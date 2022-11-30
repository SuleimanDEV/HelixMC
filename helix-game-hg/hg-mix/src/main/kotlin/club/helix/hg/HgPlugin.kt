package club.helix.hg

import club.helix.bukkit.HelixBukkit
import club.helix.components.server.provider.HardcoreGamesProvider
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class HgPlugin: JavaPlugin() {

    val apiBukkit = server.pluginManager.getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid api (HelixBukkit)")
    val serverProvider get() = apiBukkit.getCurrentCastServer<HardcoreGamesProvider>()

    val game = HgGame()
    val doubleKit = Random().nextInt(100) >= 50

    lateinit var spawnLocation: Location
        private set

    override fun onEnable() {
        loadSpawnLocation()
        loadListeners()

        GameRunnable(this).runTaskTimer(this, 0, 20)
        server.consoleSender.sendMessage("§9§lHG: §fPlugin habilitado! §9[v${description.version}]")
    }

    private fun loadListeners(): Unit = server.pluginManager.run {

    }

    private fun loadSpawnLocation() {
        val world = server.worlds.first()
        spawnLocation = Location(world, 0.0, world.getHighestBlockYAt(0, 0).toDouble(), 0.0)
    }
}