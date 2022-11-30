package club.helix.pvp.fps

import club.helix.bukkit.HelixBukkit
import club.helix.pvp.api.GameAPI
import club.helix.pvp.fps.command.SpawnCMD
import club.helix.pvp.fps.handle.ServerSpawnHandle
import club.helix.pvp.fps.listener.*
import club.helix.pvp.fps.npc.LeaveGameNPC
import club.helix.pvp.fps.scoreboard.FpsScoreboard
import club.helix.pvp.fps.util.CombatLog
import org.bukkit.plugin.java.JavaPlugin

class PvPFps: JavaPlugin() {
    companion object {
        val instance: PvPFps get() = getPlugin(PvPFps::class.java)
    }

    val apiBukkit = server.pluginManager.getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid api bukkit (HelixBukkit)")
    val arenaPlayers = mutableListOf<String>()
    val combatLog = CombatLog()
    val serverSpawnHandle = ServerSpawnHandle(this)
    val scoreboard = FpsScoreboard()

    override fun onEnable() {
        server.worlds.forEach {
            it.time = 1000
            it.setGameRuleValue("doDaylightCycle", "false")
            it.setGameRuleValue("naturalRegeneration", "false")
            it.setGameRuleValue("doMobSpawning", "false")
        }
        scoreboard.startUpdater(this, 6)
        loadListeners()

        apiBukkit.commandMap.createCommand(SpawnCMD(this))
        LeaveGameNPC().load()

        GameAPI(this).onEnable()
        server.consoleSender.sendMessage("§2§lPVP-FPS: §fPlugin habilitado! §2[v${description.version}]")
    }

    private fun loadListeners(): Unit = server.pluginManager.run {
        registerEvents(UserDeathListener(this@PvPFps), this@PvPFps)
        registerEvents(UserJoinArenaListener(this@PvPFps), this@PvPFps)
        registerEvents(UserJoinListener(this@PvPFps), this@PvPFps)
        registerEvents(UserLeaveListener(this@PvPFps), this@PvPFps)
        registerEvents(ClearArenaDropsListener(this@PvPFps), this@PvPFps)
        registerEvents(CombatLogListener(this@PvPFps), this@PvPFps)
        registerEvents(ClanReceiveExpListener(this@PvPFps), this@PvPFps)
        registerEvents(UserLastKillOnQuitListener(this@PvPFps), this@PvPFps)
        registerEvents(UserHologramRankingListener(this@PvPFps), this@PvPFps)
        registerEvents(UserCancelDamageListener(), this@PvPFps)
        registerEvents(ServerEssentialsListener(), this@PvPFps)
        registerEvents(JoinQuitMessageListener(), this@PvPFps)
        registerEvents(RefillSignListener(), this@PvPFps)
        registerEvents(SoupSystemListener(), this@PvPFps)
    }
}