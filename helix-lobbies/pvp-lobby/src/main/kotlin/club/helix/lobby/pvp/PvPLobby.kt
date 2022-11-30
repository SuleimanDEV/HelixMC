package club.helix.lobby.pvp

import club.helix.bukkit.HelixBukkit
import club.helix.lobby.provider.npc.NpcLoader
import club.helix.lobby.pvp.listener.InteractLobbyItemsListener
import club.helix.lobby.pvp.listener.SpawnUserDistanceListener
import club.helix.lobby.pvp.listener.UserJoinListener
import club.helix.lobby.pvp.listener.UserPortalListener
import club.helix.lobby.pvp.npc.ArenaNpc
import club.helix.lobby.pvp.npc.FpsNpc
import club.helix.lobby.pvp.npc.LavaChallengeNpc
import club.helix.lobby.pvp.scoreboard.PvPScoreboard
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

class PvPLobby: JavaPlugin() {

    val spawnLocation get() = Location(HelixBukkit.instance.world,
        0.6127461170327951, 75.0, 0.48156954126732177, 269.84985f, -2.1000361f)
    val scoreboard = PvPScoreboard()

    override fun onEnable() {
        server.worlds.forEach { it.time = 1000 }
        loadListeners()
        scoreboard.startUpdater(this, 3)

        NpcLoader.load(ArenaNpc(), FpsNpc(), LavaChallengeNpc())
        server.consoleSender.sendMessage("§3§lPVP-LOBBY: §fPlugin habilitado! §3[v${description.version}]")
    }

    private fun loadListeners() = Bukkit.getPluginManager().let {
        it.registerEvents(UserJoinListener(this), this)
        it.registerEvents(SpawnUserDistanceListener(this), this)
        it.registerEvents(UserPortalListener(this), this)
        it.registerEvents(InteractLobbyItemsListener(), this)
    }
}