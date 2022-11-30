package club.helix.bukkit.nms

import club.helix.bukkit.util.ReflectionUtil
import org.bukkit.entity.Player

class TabListNMS {
    companion object {
        fun set(player: Player, header: String, footer: String) {
            val packetClass = ReflectionUtil.getNMSClass("PacketPlayOutPlayerListHeaderFooter")
            val packet = packetClass.newInstance()

            val icbc = ReflectionUtil.getNMSClass("IChatBaseComponent")
            val icbcBuilderField = icbc.declaredClasses.first().getMethod("a", String::class.java)

            ReflectionUtil.setField(
                packet, packetClass.getDeclaredField("a"),
                icbcBuilderField.invoke(null, "{'text': '$header'}")
            )
            ReflectionUtil.setField(
                packet, packetClass.getDeclaredField("b"),
                icbcBuilderField.invoke(null, "{'text': '$footer'}")
            )

            ReflectionUtil.sendPacket(player, packet)
        }
    }
}