package club.helix.bukkit.npc

import club.helix.bukkit.util.ReflectionUtil
import club.helix.hologram.Hologram
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.event.NPCClickEvent
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.npc.NPCDataStore
import net.citizensnpcs.api.npc.NPCRegistry
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam
import net.minecraft.server.v1_8_R3.ScoreboardTeam
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.util.function.Consumer


open class HelixNPC(
    val name: String = "§8[NPC-${npcs.size}]",
    val location: Location,
    private val skinOrigin: String,
    private val skinValue: String,
    private val skinSignature: String,
    open var hologram: Hologram? = null,
) {
    companion object {
        const val dataKey = "helix-npc"
        val npcs = mutableSetOf<HelixNPC>()

        fun load() {
            CitizensAPI.createNamedNPCRegistry("Helix", object: NPCDataStore {
                var id: Int = 0

                override fun clearData(p0: NPC) {}
                override fun createUniqueNPCId(p0: NPCRegistry): Int = id++
                override fun loadInto(p0: NPCRegistry) {}
                override fun saveToDisk() {}
                override fun saveToDiskImmediate() {}
                override fun store(p0: NPC?) {}
                override fun storeAll(p0: NPCRegistry?) {}
                override fun reloadFromSource() {}
            })
        }
    }

    init { npcs.add(this) }

    var consumer: Consumer<NPCClickEvent>? = null

    val npc get() = CitizensAPI.getNamedNPCRegistry("Helix").createNPC(EntityType.PLAYER, name).apply {
        isProtected = true

        data().apply {
            setPersistent(NPC.PLAYER_SKIN_USE_LATEST, false)
            setPersistent("cached-skin-uuid-name", skinOrigin)
            setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, skinOrigin)
            setPersistent(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_METADATA, skinValue)
            setPersistent(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_SIGN_METADATA, skinSignature)
            setPersistent(dataKey, name)
        }

        if (isSpawned) {
            storedLocation.clone().apply { despawn(); spawn(this) }
        }
    }

    fun spawn() {
        npc.spawn(location)
        hologram?.spawn()
    }

    fun spawn(consumer: Consumer<NPCClickEvent>) {
        this.consumer = consumer
        npc.spawn(location)
        hologram?.spawn()
    }

    fun hideName(player: Player) {
        val scoreboardTeam = ScoreboardTeam((Bukkit.getScoreboardManager().mainScoreboard as CraftScoreboard).handle, name).apply {
            nameTagVisibility = ScoreboardTeamBase.EnumNameTagVisibility.NEVER
        }

        PacketPlayOutScoreboardTeam(scoreboardTeam, 1).apply{
            ReflectionUtil.sendPacket(player, this)
        }
        PacketPlayOutScoreboardTeam(scoreboardTeam, 0).apply {
            ReflectionUtil.sendPacket(player, this)
        }
        PacketPlayOutScoreboardTeam(scoreboardTeam, mutableListOf(name), 3)
            .apply { ReflectionUtil.sendPacket(player, this) }
    }
}