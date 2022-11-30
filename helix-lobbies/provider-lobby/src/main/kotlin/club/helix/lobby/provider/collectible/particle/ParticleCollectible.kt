package club.helix.lobby.provider.collectible.particle

import club.helix.lobby.provider.ProviderLobby
import club.helix.lobby.provider.collectible.Collectible
import club.helix.lobby.provider.collectible.CollectibleCategory
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

abstract class ParticleCollectible(
    id: String,
    name: String,
    private val runnableTicks: Long,
    icon: ItemStack,
    createdAt: Long
): Collectible(
    id,
    name,
    CollectibleCategory.PARTICLE,
    icon,
    createdAt
) {
    protected val protocolManager: ProtocolManager = ProtocolLibrary.getProtocolManager()
    open val radius = 0.5

    private var runnable: BukkitRunnable? = null

    protected val playersTime = mutableMapOf<String, Double>()

    abstract fun callRunnable(player: Player)

    fun sendServerPacket(packet: PacketContainer) = Bukkit.getOnlinePlayers().forEach {
        protocolManager.sendServerPacket(it, packet)
    }

    private fun handleRunnable() {
        runnable?.apply {
            cancel()
            runnable = null
            return
        }

        runnable = object: BukkitRunnable() {
            override fun run() {
                playersTime.keys.mapNotNull { Bukkit.getPlayer(it) }
                    .forEach(this@ParticleCollectible::callRunnable)
            }
        }.apply { runTaskTimerAsynchronously(ProviderLobby.instance, 20, runnableTicks) }
    }

    override fun onEnable(player: Player) {
        playersTime[player.name] = 0.0
        handleRunnable()
    }

    override fun onDisable(player: Player) {
        playersTime.remove(player.name)
        if (playersTime.isEmpty()) handleRunnable()
    }
}