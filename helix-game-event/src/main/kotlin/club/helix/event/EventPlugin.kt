package club.helix.event

import club.helix.bukkit.HelixBukkit
import club.helix.event.command.*
import club.helix.event.listener.*
import club.helix.event.listener.config.*
import club.helix.event.specialitem.SpecialItem
import club.helix.event.specialitem.listener.LocatorItemListener
import club.helix.event.util.StaffPermissions
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

class EventPlugin: JavaPlugin() {

    val apiBukkit = server.pluginManager.getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid api (HelixBukkit)")
    val game = EventGame()

    lateinit var spawnLocation: Location

    override fun onEnable() {
        server.worlds.forEach {
            it.time = 1000
            it.setGameRuleValue("doDaylightCycle", "false")
            it.setGameRuleValue("naturalRegeneration", "false")
            it.setGameRuleValue("doMobSpawning", "false")
        }
        spawnLocation = Location(HelixBukkit.instance.world, 0.0, HelixBukkit.instance.world.getHighestBlockYAt(0, 0).toDouble(), 0.0)

        loadListeners()

        apiBukkit.commandMap.apply {
            createCommand(GetConfigCMD(this@EventPlugin))
            createCommand(SetConfigCMD(this@EventPlugin))
            createCommand(SetTimeCMD(this@EventPlugin))
            createCommand(SetWinnerCMD(this@EventPlugin))
            createCommand(SkitCMD(this@EventPlugin))
            createCommand(SetSpawnCMD(this@EventPlugin))
            createCommand(WhitelistCMD(this@EventPlugin))
            createCommand(TpAllCMD(this@EventPlugin))
            createCommand(ClearDropsCMD(this@EventPlugin))
            createCommand(ClearBlocksCMD(this@EventPlugin))
            createCommand(SpecialItemCMD())
        }

        game.apply {
            setConfig("name", "Indefinido")
            setConfig("reward", "Indefinido")
            setConfig("drop-items-on-death", true)
            setConfig("death-message", true)
            setConfig("clear-item-on-drop", false)
            setConfig("build", false)
            setConfig("damage", false)
            setConfig("cocoa-bean-soup", true)
            setConfig("max-players", 60)
            setConfig("item-drop", true)
        }

        StaffPermissions.load()
        GameRunnable(this).runTaskTimer(this, 0, 20)
        server.consoleSender.sendMessage("§e§lEVENT: §fPlugin habilitado! §e[${description.version}]")
    }

    fun changeConfigByServer(key: String, value: Any) {
        game.setConfig(key, value)
        notifyPlayers.forEach {
            it.sendMessage("§7[§c!§7] §7\"${key.lowercase()}\" foi alterado para §f$value §7pelo servidor.")
        }
    }

    val notifyPlayers get() = server.onlinePlayers.filter { it.hasPermission("helix.event.notify") }

    fun updateServerState() {
        //apiBukkit.currentServer.available = game.state != EventGame.State.WHITELIST
    }

    private fun loadListeners() = server.pluginManager.run {
        registerEvents(GameStateChangeListener(this@EventPlugin), this@EventPlugin)
        registerEvents(GamePlayingPlayerJoinListener(this@EventPlugin), this@EventPlugin)
        registerEvents(MaxPlayersListener(this@EventPlugin), this@EventPlugin)
        registerEvents(UserJoinListener(this@EventPlugin), this@EventPlugin)
        registerEvents(UserQuitListener(this@EventPlugin), this@EventPlugin)
        registerEvents(UserGameWhitelistListener(this@EventPlugin), this@EventPlugin)
        registerEvents(GameDamageConfigListener(this@EventPlugin), this@EventPlugin)
        registerEvents(GameSpectatorJoinListener(this@EventPlugin), this@EventPlugin)
        registerEvents(GamePlayerDeathListener(this@EventPlugin), this@EventPlugin)
        registerEvents(SpectatorItemsListener(this@EventPlugin), this@EventPlugin)
        registerEvents(CocoaBeanSoupConfigListener(this@EventPlugin), this@EventPlugin)
        registerEvents(ClearItemOnDropConfigListener(this@EventPlugin), this@EventPlugin)
        registerEvents(ItemDropConfigListener(this@EventPlugin), this@EventPlugin)
        registerEvents(SpectatorBlockEventListener(this@EventPlugin), this@EventPlugin)
        registerEvents(GameBlockListener(this@EventPlugin), this@EventPlugin)
        registerEvents(LocatorItemListener(this@EventPlugin), this@EventPlugin)
        registerEvents(SoupSystemListener(), this@EventPlugin)
        registerEvents(GameBuildConfigListener(), this@EventPlugin)
        registerEvents(GameStartListener(), this@EventPlugin)
        registerEvents(ServerEssentialsListener(), this@EventPlugin)
        registerEvents(GameStartingListener(), this@EventPlugin)
        registerEvents(GameEndListener(), this@EventPlugin)
    }
}