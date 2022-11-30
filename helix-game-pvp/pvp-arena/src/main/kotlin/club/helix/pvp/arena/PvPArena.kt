package club.helix.pvp.arena

import club.helix.bukkit.HelixBukkit
import club.helix.pvp.api.GameAPI
import club.helix.pvp.arena.listener.ClanReceiveExpListener
import club.helix.pvp.arena.listener.UserLastKillOnQuitListener
import club.helix.pvp.arena.command.FeastCMD
import club.helix.pvp.arena.command.SpawnCMD
import club.helix.pvp.arena.feast.FeastRunnable
import club.helix.pvp.arena.feast.item.TNTPrimedItem
import club.helix.pvp.arena.handle.ServerSpawnHandle
import club.helix.pvp.arena.kit.ArenaKit
import club.helix.pvp.arena.listener.*
import club.helix.pvp.arena.npc.LeaveGameNPC
import club.helix.pvp.arena.recipe.CocoaBeanSoup
import club.helix.pvp.arena.scoreboard.ArenaScoreboard
import club.helix.pvp.arena.util.CombatLog
import club.helix.pvp.arena.vip.RanksKitsPermission
import org.bukkit.plugin.java.JavaPlugin

class PvPArena: JavaPlugin() {
    companion object {
        val instance get(): PvPArena = getPlugin(PvPArena::class.java)
    }

    val apiBukkit = server.pluginManager.getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid api (HelixBukkit)")
    val arenaPlayers = mutableListOf<ArenaPlayer>()
    val arenaUserKits = ArenaUserKits(apiBukkit.components)
    val scoreboard = ArenaScoreboard(this)
    val combatLog = CombatLog()
    val serverSpawnHandle = ServerSpawnHandle(this)
    val feastRunnable = FeastRunnable()

    override fun onEnable() {
        server.worlds.forEach {
            it.time = 1000
            it.setGameRuleValue("doDaylightCycle", "false")
            it.setGameRuleValue("naturalRegeneration", "false")
            it.setGameRuleValue("doMobSpawning", "false")
        }
        serverSpawnHandle.spawnLocation.apply {
            HelixBukkit.instance.world.setSpawnLocation(x.toInt(), y.toInt(), z.toInt()) }
        arenaUserKits.createTableIfNotExists()
        scoreboard.startAsyncUpdater(this, 1)

        loadListeners()
        ArenaKit.loadListeners(this)
        apiBukkit.commandMap.createCommand(SpawnCMD(this))
        apiBukkit.commandMap.createCommand(FeastCMD(this))
        CocoaBeanSoup().register()

        LeaveGameNPC().load()
        RanksKitsPermission.load()
        feastRunnable.runTaskTimer(this, 0, 20L)

        GameAPI(this).onEnable()
        server.consoleSender.sendMessage("§a§lPVP-ARENA: §fPlugin habilitado! §a[v${description.version}]")
    }

    private fun loadListeners() = server.pluginManager.run {
        registerEvents(OpenKitInventoryListener(this@PvPArena), this@PvPArena)
        registerEvents(UserJoinListener(this@PvPArena), this@PvPArena)
        registerEvents(UserQuitListener(this@PvPArena), this@PvPArena)
        registerEvents(UserLastKillOnQuitListener(this@PvPArena), this@PvPArena)
        registerEvents(UserJoinPvPListener(this@PvPArena), this@PvPArena)
        registerEvents(UserDeathListener(this@PvPArena), this@PvPArena)
        registerEvents(CancelSpawnDamageListener(this@PvPArena), this@PvPArena)
        registerEvents(ClearArenaDropsListener(this@PvPArena), this@PvPArena)
        registerEvents(PvPCombatListener(this@PvPArena), this@PvPArena)
        registerEvents(ClanReceiveExpListener(this@PvPArena), this@PvPArena)
        registerEvents(OpenBuyKitsInventoryListener(this@PvPArena), this@PvPArena)
        registerEvents(UserHologramRankingListener(this@PvPArena), this@PvPArena)
        registerEvents(ServerEssentialsListener(), this@PvPArena)
        registerEvents(BackLobbyItemListener(), this@PvPArena)
        registerEvents(ChangeLavaDamageListener(), this@PvPArena)
        registerEvents(FindCompassPlayerListener(), this@PvPArena)
        registerEvents(BlockJumpListener(), this@PvPArena)
        registerEvents(BlockIgniteListener(), this@PvPArena)
        registerEvents(SoupSystemListener(), this@PvPArena)
        registerEvents(TNTPrimedItem(this@PvPArena), this@PvPArena)
    }
}