package club.helix.pvp.lava

import club.helix.bukkit.HelixBukkit
import club.helix.pvp.lava.command.SpawnCMD
import club.helix.pvp.lava.listener.*
import club.helix.pvp.lava.npc.LeaveGameNPC
import club.helix.pvp.lava.npc.SettingsNPC
import club.helix.pvp.lava.scoreboard.LavaScoreboard
import club.helix.pvp.lava.util.ServerSpawn
import org.bukkit.plugin.java.JavaPlugin

class PvPLava: JavaPlugin() {
    companion object {
        val instance get() = getPlugin(PvPLava::class.java)
    }

    val apiBukkit = server.pluginManager.getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid api (HelixBukkit)")
    val scoreboard = LavaScoreboard()
    val serverSpawn = ServerSpawn()

    val lavaPlayers = mutableListOf<LavaPlayer>()

    override fun onEnable() {
        server.worlds.forEach {
            it.time = 1000
            it.setGameRuleValue("doDaylightCycle", "false")
            it.setGameRuleValue("naturalRegeneration", "false")
            it.setGameRuleValue("doMobSpawning", "false")
        }
        apiBukkit.commandMap.createCommand(SpawnCMD(this))

        scoreboard.startUpdater(this, 1)
        loadListeners()
        SettingsNPC().load()
        LeaveGameNPC().load()
        server.consoleSender.sendMessage("§6§lLAVA CHALLENGE: §fPlugin habilitado! §6[v${description.version}]")
    }

    private fun loadListeners() = server.pluginManager.apply {
        registerEvents(UserJoinListener(this@PvPLava), this@PvPLava)
        registerEvents(UserDeathListener(this@PvPLava), this@PvPLava)
        registerEvents(ClearDropsListener(), this@PvPLava)
        registerEvents(RefillSignListener(), this@PvPLava)
        registerEvents(UserQuitListener(), this@PvPLava)
        registerEvents(ServerEssentialsListener(), this@PvPLava)
        registerEvents(SelectLevelItemListener(), this@PvPLava)
        registerEvents(UserDamageListener(), this@PvPLava)
        registerEvents(BlockIgniteListener(), this@PvPLava)
        registerEvents(BlockJumpListener(), this@PvPLava)
        registerEvents(ChangeLavaDamageListener(), this@PvPLava)
        registerEvents(SoupSystemListener(), this@PvPLava)
        registerEvents(BackLobbyItemListener(), this@PvPLava)
    }
}