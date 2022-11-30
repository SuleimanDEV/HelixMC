package club.helix.bungeecord

import club.helix.bungeecord.automessage.AutoMessageTask
import club.helix.bungeecord.precommand.BungeeCommandMap
import club.helix.bungeecord.precommand.executor.BungeeCommandExecutor
import club.helix.bungeecord.pubsub.UserGroupUpdateService
import club.helix.bungeecord.pubsub.UserUpdateService
import club.helix.bungeecord.shop.CheckShopVipTask
import club.helix.bungeecord.user.BungeePermissionLoader
import club.helix.components.HelixComponents
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import org.reflections8.Reflections

class HelixBungee: Plugin() {
    companion object {
        lateinit var instance: HelixBungee
    }

    val components = HelixComponents(BungeePermissionLoader(this))
    val commandMap = BungeeCommandMap(this)

    override fun onLoad() = components.start()

    override fun onEnable() {
        instance = this
        components.redisPool.resource.use { it.flushDB() }

        registerCommands()
        registerListeners()

        components.pubsubManager.register(
            UserGroupUpdateService(),
            UserUpdateService(components)
        )

        proxy.console.sendMessage(TextComponent("§5§lAPI: §fBiblioteca habilitada! §5[v${description.version}]"))
        CheckShopVipTask(this).run()
        AutoMessageTask(this).run()
        components.updateServers()
    }

    private fun registerCommands() = Reflections("club.helix.bungeecord.command").getSubTypesOf(BungeeCommandExecutor::class.java).forEach {
        val constructor = it.constructors.first()
        val command = if (constructor.parameterCount == 1) it.getConstructor(HelixBungee::class.java).newInstance(this)
            else it.newInstance()
        commandMap.createCommand(command)
    }

    private fun registerListeners() = Reflections("club.helix.bungeecord.listener").getSubTypesOf(Listener::class.java).forEach {
        val constructor = it.constructors.first()
        val listener = if (constructor.parameterCount == 1) it.getConstructor(HelixBungee::class.java).newInstance(this)
            else it.newInstance()
        proxy.pluginManager.registerListener(this, listener)
    }

    override fun onDisable() { components.close() }
}