package club.helix.duels.gladiator

import club.helix.bukkit.HelixBukkit
import club.helix.duels.api.DuelsAPI
import club.helix.duels.api.listener.CancelCraftItemListener
import club.helix.duels.api.listener.GamePrivateChatListener
import club.helix.duels.api.listener.SoupSystemListener
import club.helix.duels.gladiator.listener.*
import club.helix.duels.gladiator.recipe.CocoaBeanSoup
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class GladiatorPlugin: JavaPlugin() {
    companion object {
        val instance: GladiatorPlugin get() = getPlugin(GladiatorPlugin::class.java)
    }

    val apiBukkit = server.pluginManager.getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid bukkit api (HelixBukkt)")
    val duelsAPI = object: DuelsAPI<GladiatorGame>(this, apiBukkit.components) {
        override fun newGame() = GladiatorGame().apply { registerGame(this) }
    }
    val arenaSchematic = File(dataFolder, "gladiator.schematic")

    override fun onEnable() {
        server.worlds.forEach {
            it.setGameRuleValue("doDaylightCycle", "false")
            it.setGameRuleValue("naturalRegeneration", "false")
            it.setGameRuleValue("doMobSpawning", "false")
            it.time = 1000
        }
        duelsAPI.onEnable()

        loadListeners()
        DuelsGameRunnable(this).runTaskTimer(this, 0, 20)
        CocoaBeanSoup().register()
        server.consoleSender.sendMessage("§e§lDUELS UHC: §fPlugin habilitado! §e[v${description.version}]")
    }

    private fun loadListeners() = server.pluginManager.run {
        registerEvents(GameEndListener(this@GladiatorPlugin), this@GladiatorPlugin)
        registerEvents(GamePrivateChatListener(duelsAPI), this@GladiatorPlugin)
        registerEvents(ArenaBlocksListener(duelsAPI), this@GladiatorPlugin)
        registerEvents(GameArenaGeneratorListener(this@GladiatorPlugin), this@GladiatorPlugin)
        registerEvents(DeleteArenaListener(this@GladiatorPlugin), this@GladiatorPlugin)
        registerEvents(GameSwitchScoreboardListener(), this@GladiatorPlugin)
        registerEvents(CancelCraftItemListener(), this@GladiatorPlugin)
        registerEvents(UserJoinGameListener(), this@GladiatorPlugin)
        registerEvents(GameStartListener(), this@GladiatorPlugin)
        registerEvents(SoupSystemListener(), this@GladiatorPlugin)
    }
}