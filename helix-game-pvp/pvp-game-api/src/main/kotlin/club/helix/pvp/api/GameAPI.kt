package club.helix.pvp.api

import club.helix.bukkit.HelixBukkit
import club.helix.pvp.api.command.RankListCMD
import club.helix.pvp.api.command.RankingCMD
import club.helix.pvp.api.listener.UserChatRankListener
import club.helix.pvp.api.listener.UserJoinListener
import club.helix.pvp.api.listener.UserKillPlayerListener
import org.bukkit.plugin.java.JavaPlugin

class GameAPI(private val plugin: JavaPlugin) {

    val apiBukkit = plugin.server.pluginManager.getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid api (HelixBukkit)")

    fun onEnable() {
        loadCommands()
        loadListeners()
    }

    private fun loadCommands() = arrayOf(
        RankListCMD(),
        RankingCMD()
    ).forEach { apiBukkit.commandMap.createCommand(it) }

    private fun loadListeners() = arrayOf(
        UserJoinListener(this),
        UserChatRankListener(this),
        UserKillPlayerListener(this)
    ).forEach { plugin.server.pluginManager.registerEvents(it, plugin) }
}