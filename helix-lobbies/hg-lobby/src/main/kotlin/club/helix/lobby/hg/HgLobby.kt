package club.helix.lobby.hg

import club.helix.components.server.HelixServer
import club.helix.components.server.provider.HardcoreGamesProvider
import club.helix.lobby.hg.listener.InteractLobbyItemsListener
import club.helix.lobby.hg.listener.UserJoinListener
import club.helix.lobby.hg.npc.HgMixNpc
import club.helix.lobby.hg.scoreboard.HgScoreboard
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class HgLobby: JavaPlugin() {

    val scoreboard = HgScoreboard()

    override fun onEnable() {
        loadListeners()
        scoreboard.startUpdater(this, 4)

        HgMixNpc().load()
        server.consoleSender.sendMessage("§3§lHG-LOBBY: §fPlugin habilitado! §3[v${description.version}]")
        HelixServer.HG.providers(HelixServer.Category.HG_MIX).filterIsInstance<HardcoreGamesProvider>().forEach {
            server.consoleSender.sendMessage("§e${it.displayName} ${it.state} ${it.time}")
        }
    }

    private fun loadListeners(): Unit = Bukkit.getPluginManager().run {
        registerEvents(UserJoinListener(this@HgLobby), this@HgLobby)
        registerEvents(InteractLobbyItemsListener(), this@HgLobby)
    }
}