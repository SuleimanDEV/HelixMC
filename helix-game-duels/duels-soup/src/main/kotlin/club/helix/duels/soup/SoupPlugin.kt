package club.helix.duels.soup

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.util.Location
import club.helix.duels.api.DuelsAPI
import club.helix.duels.api.game.DuelsGame
import club.helix.duels.api.listener.ClearDropsListener
import club.helix.duels.api.listener.GamePrivateChatListener
import club.helix.duels.api.listener.SoupSystemListener
import club.helix.duels.soup.listener.*
import org.bukkit.plugin.java.JavaPlugin

class SoupPlugin: JavaPlugin() {

    val apiBukkit = server.pluginManager.getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid bukkit api (HelixBukkt)")
    val duelsAPI = object: DuelsAPI<DuelsGame>(this, apiBukkit.components) {
        override fun newGame() = SoupGame().apply { registerGame(this) }
    }
    val spawnLocation = Location(0.4203235836888013, 65.0, 0.484241042693363, 89.70411f, -1.2000006f)
    val pos1 = Location(0.4682703167509227, 65.0, -32.46302844489175, 0.004113412f, -1.050012f)
    val pos2 = Location(0.4498941082017727, 65.0, 33.491345925489554, 180.45412f, -3.0000074f)

    override fun onEnable() {
        server.worlds.forEach {
            it.setGameRuleValue("doDaylightCycle", "false")
            it.setGameRuleValue("naturalRegeneration", "false")
            it.setGameRuleValue("doMobSpawning", "false")
            it.time = 1000
        }
        duelsAPI.onEnable()

        loadListeners()
        SoupGameRunnable(this).runTaskTimer(this, 0, 20L)
        server.consoleSender.sendMessage("§3§lDUELS SOUP: §fPlugin habilitado! §3[v${description.version}]")
    }

    private fun loadListeners() = server.pluginManager.run {
        registerEvents(GameEndListener(this@SoupPlugin), this@SoupPlugin)
        registerEvents(GameStartListener(this@SoupPlugin), this@SoupPlugin)
        registerEvents(UserJoinGameListener(this@SoupPlugin), this@SoupPlugin)
        registerEvents(GamePrivateChatListener(duelsAPI), this@SoupPlugin)
        registerEvents(DamageConfigurationListener(), this@SoupPlugin)
        registerEvents(ClearDropsListener(), this@SoupPlugin)
        registerEvents(SoupSystemListener(), this@SoupPlugin)
        registerEvents(GameSwitchScoreboardListener(), this@SoupPlugin)
    }
}