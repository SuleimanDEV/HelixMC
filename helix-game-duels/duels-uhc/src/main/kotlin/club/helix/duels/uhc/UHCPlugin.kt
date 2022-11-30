package club.helix.duels.uhc

import club.helix.bukkit.HelixBukkit
import club.helix.duels.api.DuelsAPI
import club.helix.duels.api.listener.CancelCraftItemListener
import club.helix.duels.api.listener.GamePrivateChatListener
import club.helix.duels.uhc.listener.*
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class UHCPlugin: JavaPlugin() {
    companion object {
        val instance: UHCPlugin get() = getPlugin(UHCPlugin::class.java)
    }

    val apiBukkit = server.pluginManager.getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid bukkit api (HelixBukkt)")
    val duelsAPI = object: DuelsAPI<UHCGame>(this, apiBukkit.components) {
        override fun newGame() = UHCGame().apply { registerGame(this) }
    }
    val arenaSchematic = File(dataFolder, "arena.schematic")

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
        server.consoleSender.sendMessage("§e§lDUELS UHC: §fPlugin habilitado! §e[v${description.version}]")
    }

    private fun loadListeners() = server.pluginManager.run {
        registerEvents(GameEndListener(this@UHCPlugin), this@UHCPlugin)
        registerEvents(GamePrivateChatListener(duelsAPI), this@UHCPlugin)
        registerEvents(ArenaBlocksListener(duelsAPI), this@UHCPlugin)
        registerEvents(GameArenaGeneratorListener(this@UHCPlugin), this@UHCPlugin)
        registerEvents(DeleteArenaListener(this@UHCPlugin), this@UHCPlugin)
        registerEvents(CancelCraftItemListener(), this@UHCPlugin)
        registerEvents(GameSwitchScoreboardListener(), this@UHCPlugin)
        registerEvents(UserJoinGameListener(), this@UHCPlugin)
        registerEvents(GameStartListener(), this@UHCPlugin)
        registerEvents(InstantAppleListener(), this@UHCPlugin)
    }
}