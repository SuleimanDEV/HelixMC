package club.helix.lobby.duels

import club.helix.bukkit.HelixBukkit
import club.helix.lobby.duels.command.SpectateCMD
import club.helix.lobby.duels.listener.DuelsChatListener
import club.helix.lobby.duels.listener.InteractLobbyItemsListener
import club.helix.lobby.duels.listener.UserJoinListener
import club.helix.lobby.duels.npc.*
import club.helix.lobby.duels.scoreboard.DuelsScoreboard
import club.helix.lobby.duels.util.HologramTopLevel
import club.helix.lobby.provider.listener.SlimeBlockJumpListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class DuelsLobby: JavaPlugin() {

    val apiBukkit = server.pluginManager.getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid api bukkit (HelixBukkit)")
    val scoreboard = DuelsScoreboard()

    override fun onEnable() {
        scoreboard.startUpdater(this, 5)
        loadListeners()
        HologramTopLevel(this).runTask()

        UhcNpc().load(); LavaNpc().load()
        SoupNpc().load(); SumoNpc().load()
        GladiatorNpc().load(); StatisticPlayerNpc().load()
        apiBukkit.commandMap.createCommand(SpectateCMD(apiBukkit.components))
        server.consoleSender.sendMessage("§e§lDUELS-LOBBY: §fPlugin habilitado! §e[${description.version}]")
    }

    private fun loadListeners() = Bukkit.getPluginManager().let {
        it.registerEvents(UserJoinListener(this), this)
        it.registerEvents(InteractLobbyItemsListener(), this)
        it.registerEvents(DuelsChatListener(), this)
    }
}