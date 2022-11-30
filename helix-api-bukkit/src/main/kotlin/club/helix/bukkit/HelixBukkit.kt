package club.helix.bukkit

import club.helix.bukkit.hologram.HelixHologram
import club.helix.bukkit.npc.HelixNPC
import club.helix.bukkit.precommand.BukkitCommandMap
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.pubsub.UserGroupUpdateService
import club.helix.bukkit.task.CheckUserGroupTask
import club.helix.bukkit.task.HelixTask
import club.helix.bukkit.task.RemapCommandsTask
import club.helix.bukkit.user.BukkitPermissionLoader
import club.helix.bukkit.util.HelixPermission
import club.helix.bukkit.util.ServerChat
import club.helix.components.HelixComponents
import club.helix.components.server.HelixServer
import club.helix.components.server.HelixServerProvider
import club.helix.hologram.HologramListener
import club.helix.inv.HelixInvsPlugin
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.craftbukkit.v1_8_R3.CraftServer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener
import org.reflections8.Reflections

class HelixBukkit: JavaPlugin(), PluginMessageListener {

    companion object {
        val instance: HelixBukkit get() : HelixBukkit = (getPlugin(HelixBukkit::class.java))
    }

    val components = HelixComponents(BukkitPermissionLoader(this))
    val world: World get() = server.worlds.first()
    val permissionManager = HelixPermission(this)
    val serverChat = ServerChat()
    val commandMap = BukkitCommandMap()

    lateinit var currentServer: HelixServerProvider
        private set

    inline fun <reified T: HelixServerProvider> getCurrentCastServer() = currentServer as? T
        ?: throw ClassCastException("invalid cast server")

    override fun onLoad() = components.start()

    override fun onEnable() {
        val serverName = config.getString("server-name") ?: throw NullPointerException("invalid server name")
        currentServer = HelixServer.getServer(serverName)
            ?: throw NullPointerException("invalid current server")

        server.worlds.forEach { it.isAutoSave = false; it.entities.forEach(Entity::remove) }
        saveDefaultConfig()

        currentServer.apply {
            maxPlayers = server.maxPlayers
            online = true
            components.callUpdateServer(this)
        }

        server.messenger.let {
            it.registerIncomingPluginChannel(this, "BungeeCord", this)
            it.registerOutgoingPluginChannel(this, "BungeeCord")
        }

        HelixNPC.load()
        HologramListener(this).start()
        HelixInvsPlugin.instance().register(this)
        registerCommands("club.helix.bukkit.command")
        registerListeners("club.helix.bukkit.command")
        registerListeners("club.helix.bukkit.listener")
        loadTasks()

        (Bukkit.getServer() as CraftServer).handle.server.autosavePeriod = -1
        server.consoleSender.sendMessage("§5§lAPI: §fBiblioteca habilitada! §5[v${description.version}]")
        server.consoleSender.sendMessage("§5§lAPI: §fIdentidade: §5${currentServer.displayName} (${currentServer.serverName})")
        components.pubsubManager.register(UserGroupUpdateService(this))
        components.updateServers()
    }

    override fun onDisable() {
        currentServer.apply { online = false; onlinePlayers.clear(); components.callUpdateServer(this) }
        HelixHologram.holograms.forEach(HelixHologram::destroy)
        Bukkit.getOnlinePlayers().forEach { it.kickPlayer("§c${currentServer.serverName} está desligando...") }

        components.close()
        commandMap.commands.forEach { commandMap.unregisterCommand(it.key) }
        server.scheduler.cancelAllTasks()
    }

    fun registerCommands(packageName: String) = Reflections(packageName).getSubTypesOf(BukkitCommandExecutor::class.java).forEach {
        val constructor = it.constructors.first()
        val command = if (constructor.parameterCount == 1) it.getConstructor(HelixBukkit::class.java).newInstance(this)
            else it.newInstance()
        commandMap.createCommand(command)
    }

    fun registerListeners(packageName: String) = Reflections(packageName).getSubTypesOf(Listener::class.java).forEach {
        val constructor = it.constructors.first()
        val command = if (constructor.parameterCount == 1) it.getConstructor(HelixBukkit::class.java).newInstance(this)
            else it.newInstance()
        server.pluginManager.registerEvents(command, this)
    }

    private fun loadTasks() = arrayOf(
        CheckUserGroupTask(this),
        RemapCommandsTask(this)
    ).forEach(HelixTask::start)

    override fun onPluginMessageReceived(p0: String?, p1: Player?, p2: ByteArray?) {}
}