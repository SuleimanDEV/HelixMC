package club.helix.bukkit.nms

import club.helix.bukkit.event.PlayerTagHandleEvent
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.util.ReflectionUtil
import club.helix.components.account.HelixRank
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class NameTagNMS {
    enum class Reason {
        TAG, SUFFIX, CUSTOM;
    }

    class NametagData(
        val order: Int,
        var prefix: String,
        var suffix: String = ""
    )

    companion object {
        val data = mutableMapOf<String, NametagData>()

        private val LETTERS = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p")


        fun setSuffix(player: Player, suffix: String) = data[player.name]?.apply {
            this.suffix = suffix
            setNametag(player, prefix, suffix, order, Reason.SUFFIX)
        }

        fun setNametag(player: Player, tag: HelixRank, reason: Reason) =
            setNametag(player, tag.prefix, data[player.name]?.suffix ?: "", tag.ordinal, reason)

        fun setNametag(player: Player, prefix: String, order: Int, reason: Reason) =
            setNametag(player, prefix, "", order, reason)

        fun setNametag(player: Player, prefix: String, suffix: String, order: Int, reason: Reason) {
            val event = PlayerTagHandleEvent(player, reason)
            Bukkit.getPluginManager().callEvent(event)

            if (event.isCancelled) return
            data[player.name] = NametagData(order, prefix, suffix)

            Bukkit.getServer().onlinePlayers.forEach {
                handle(it, data[it.name] ?: NametagData(it.account.tag.ordinal, it.account.tag.prefix).apply {
                    data[it.name] = this
                })
            }
        }

        private fun handle(player: Player, data: NametagData) {
            val packetClass = ReflectionUtil.getNMSClass("PacketPlayOutScoreboardTeam")
            val packet = packetClass.newInstance()

            val randomUuid = UUID.randomUUID().toString().substring(0, 15)
            val prefixFormat = data.prefix.takeIf { it.length <= 16 } ?: data.prefix.substring(0, 16)
            val suffixFormat = data.suffix.takeIf { it.length <= 12 } ?: data.suffix.substring(0, 12)
            val orderLetter = if (data.order < LETTERS.size) LETTERS[data.order] else LETTERS.last()


            ReflectionUtil.setField(packet, packetClass.getDeclaredField("a"), orderLetter + randomUuid)
            ReflectionUtil.setField(packet, packetClass.getDeclaredField("b"), player.name)
            ReflectionUtil.setField(packet, packetClass.getDeclaredField("c"), prefixFormat)
            ReflectionUtil.setField(packet, packetClass.getDeclaredField("d"), suffixFormat)
            ReflectionUtil.setField(packet, packetClass.getDeclaredField("g"), Collections.singletonList(player.name))
            ReflectionUtil.setField(packet, packetClass.getDeclaredField("h"), 0)
            ReflectionUtil.setField(packet, packetClass.getDeclaredField("i"), 1)
            Bukkit.getOnlinePlayers().forEach { ReflectionUtil.sendPacket(it, packet) }
        }
    }
}