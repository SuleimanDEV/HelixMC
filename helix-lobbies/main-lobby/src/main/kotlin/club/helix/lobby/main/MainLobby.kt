package club.helix.lobby.main

import club.helix.bukkit.HelixBukkit
import club.helix.lobby.main.listener.InteractLobbyItemsListener
import club.helix.lobby.main.listener.UserJoinListener
import club.helix.lobby.main.npc.DuelsNpc
import club.helix.lobby.main.npc.EventNpc
import club.helix.lobby.main.npc.HgNpc
import club.helix.lobby.main.npc.PvPNpc
import club.helix.lobby.main.scoreboard.MainScoreboard
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

class MainLobby: JavaPlugin() {

    val scoreboard = MainScoreboard()
    val spawnLocation get() = Location(HelixBukkit.instance.world,
        0.5193282648220439, 75.0, 0.46753801973552367, -0.14996338f, -2.099991f)

    override fun onEnable() {
        scoreboard.startUpdater(this, 3)
        loadListeners()

        PvPNpc().load()
        DuelsNpc().load()
        EventNpc().load()
        HgNpc().spawn()
        server.consoleSender.sendMessage("§3§lMAIN-LOBBY: §fPlugin habilitado! §3[v${description.version}]")
    }

    private fun loadListeners() = Bukkit.getPluginManager().let {
        it.registerEvents(UserJoinListener(this), this)
        it.registerEvents(InteractLobbyItemsListener(), this)
    }
}