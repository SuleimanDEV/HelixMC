package club.helix.duels.lava

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.listener.UserConfigureDamageListener
import club.helix.bukkit.util.Location
import club.helix.duels.api.DuelsAPI
import club.helix.duels.api.listener.ClearDropsListener
import club.helix.duels.api.listener.GamePrivateChatListener
import club.helix.duels.api.listener.SoupSystemListener
import club.helix.duels.lava.listener.*
import org.bukkit.plugin.java.JavaPlugin

class LavaPlugin: JavaPlugin() {

    val apiBukkit = server.pluginManager.getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid bukkit api (HelixBukkt)")
    val duelsAPI = object: DuelsAPI<LavaGame>(this, apiBukkit.components) {
        override fun newGame() = LavaGame().apply { games.add(this) }
    }
    val spawnLocation = Location(6.486444251659481, 63.0, 2.448097708039952, 179.81616f, 0.29999655f)
    val pos1 = Location(-0.7532185409156591, 63.0, 2.5229594496822965, -89.88381f, 0.900007f)
    val pos2 = Location(13.650950573385405, 63.0, 2.41883586582999, 89.21621f, 1.200001f)


    override fun onEnable() {
        server.worlds.forEach {
            it.setGameRuleValue("doDaylightCycle", "false")
            it.setGameRuleValue("naturalRegeneration", "false")
            it.setGameRuleValue("doMobSpawning", "false")
            it.time = 1000
        }
        duelsAPI.onEnable()

        loadListeners()
        LavaGameRunnable(this).runTaskTimer(this, 0, 20L)
        server.consoleSender.sendMessage("§6§lDUELS LAVA: §fPlugin habilitado! §6[v${description.version}]")
    }

    private fun loadListeners() = server.pluginManager.run {
        registerEvents(GamePrivateChatListener(duelsAPI), this@LavaPlugin)
        registerEvents(GameEndListener(this@LavaPlugin), this@LavaPlugin)
        registerEvents(GameStartListener(this@LavaPlugin), this@LavaPlugin)
        registerEvents(UserJoinGameListener(this@LavaPlugin), this@LavaPlugin)
        registerEvents(ClearDropsListener(), this@LavaPlugin)
        registerEvents(BlockIgniteListener(), this@LavaPlugin)
        registerEvents(SoupSystemListener(), this@LavaPlugin)
        registerEvents(UserConfigureDamageListener(), this@LavaPlugin)
        registerEvents(GameSwitchScoreboardListener(), this@LavaPlugin)
    }
}