package club.helix.lobby.provider

import club.helix.bukkit.HelixBukkit
import club.helix.lobby.provider.collectible.CollectibleManager
import club.helix.lobby.provider.listener.*
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ProviderLobby: JavaPlugin() {
    companion object {
        val instance: ProviderLobby get() = getPlugin(ProviderLobby::class.java)
    }

    val apiBukkit = server.pluginManager.getPlugin("api") as? HelixBukkit
        ?: throw ClassCastException("invalid api (HelixBukkit)")

    lateinit var collectibleManager: CollectibleManager
        private set

    override fun onEnable() {
        collectibleManager = CollectibleManager(this)

        server.worlds.forEach {
            it.time = 1000
            it.setGameRuleValue("doDaylightCycle", "false")
            it.setGameRuleValue("doMobSpawning", "false")
        }

        loadListeners()
        server.consoleSender.sendMessage("§5§lLOBBY-PROVIDER: §fPlugin habilitado! §5[v${description.version}]")
    }

    private fun loadListeners() = Bukkit.getPluginManager().let {
        val components = HelixBukkit.instance.components

        it.registerEvents(InteractLobbyItemsListener(this), this)
        it.registerEvents(ChangePlayerVisibleListener(components), this)
        it.registerEvents(ServerEssentialsListener(), this)
        it.registerEvents(VipIgnoreMaxPlayersListener(), this)
        it.registerEvents(SlimeBlockJumpListener(), this)
        it.registerEvents(ServerPingListener(), this)
        it.registerEvents(UserJoinListener(), this)
        it.registerEvents(UserQuitListener(), this)
    }
}