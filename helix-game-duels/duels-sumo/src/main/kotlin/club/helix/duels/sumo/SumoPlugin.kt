package club.helix.duels.sumo

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.util.Location
import club.helix.duels.api.DuelsAPI
import club.helix.duels.api.listener.ClearDropsListener
import club.helix.duels.api.listener.GamePrivateChatListener
import club.helix.duels.sumo.listener.*
import org.bukkit.plugin.java.JavaPlugin

class SumoPlugin: JavaPlugin() {

    val apiBukkit = server.pluginManager.getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid bukkit api (HelixBukkt)")
    val duelsAPI = object: DuelsAPI<SumoGame>(this, apiBukkit.components) {
        override fun newGame() = SumoGame().apply { registerGame(this) }
    }
    val spawnLocation = Location(2.1363095302823094, 39.0, -28.518253630358522, -0.15003875f, 1.6499263f)
    val pos1 = Location(7.326283772727856, 35.0, -1.5245250339690382, 89.99994f, -3.0392408E-5f)
    val pos2 = Location(-2.455060726505259, 35.0, -1.4979292870281118, -90.150024f, 1.1999946f)

    override fun onEnable() {
        server.worlds.forEach {
            it.setGameRuleValue("doDaylightCycle", "false")
            it.setGameRuleValue("naturalRegeneration", "false")
            it.setGameRuleValue("doMobSpawning", "false")
            it.time = 1000
        }
        duelsAPI.onEnable()

        loadListeners()
        SumoGameRunnable(this).runTaskTimer(this, 0, 20L)
        server.consoleSender.sendMessage("§3§lDUELS SOUP: §fPlugin habilitado! §3[v${description.version}]")
    }

    private fun loadListeners() = server.pluginManager.run {
        registerEvents(GameEndListener(this@SumoPlugin), this@SumoPlugin)
        registerEvents(GameStartListener(this@SumoPlugin), this@SumoPlugin)
        registerEvents(UserJoinGameListener(this@SumoPlugin), this@SumoPlugin)
        registerEvents(GamePrivateChatListener(duelsAPI), this@SumoPlugin)
        registerEvents(PlayerWaterGroundListener(this@SumoPlugin), this@SumoPlugin)
        registerEvents(DamageConfigurationListener(this@SumoPlugin), this@SumoPlugin)
        registerEvents(ClearDropsListener(), this@SumoPlugin)
        registerEvents(GameSwitchScoreboardListener(), this@SumoPlugin)
    }
}