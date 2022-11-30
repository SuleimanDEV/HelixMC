package club.helix.bukkit.util

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.lang.reflect.Field

class ReflectionUtil {
    companion object {

        private val VERSION = Bukkit.getServer().javaClass.`package`.name.split(".")[3]

        fun sendPacket(player: Player, packet: Any) {
            val nmsPlayer = player.javaClass.getMethod("getHandle").invoke(player)
            val playerConnection = nmsPlayer.javaClass.getField("playerConnection")[nmsPlayer]
            val sendPacket = playerConnection.javaClass.getMethod("sendPacket", getNMSClass("Packet"))
            sendPacket.invoke(playerConnection, packet)
        }

        fun setField(key: Any, field: Field, value: Any) {
            field.isAccessible = true
            field.set(key, value)
            field.isAccessible = false
        }


        fun getNMSClass(path: String): Class<*> = Class.forName("net.minecraft.server.$VERSION.$path")
        fun getOBClass(path: String): Class<*> = Class.forName("org.bukkit.craftbukkit.$VERSION.$path")
    }
}