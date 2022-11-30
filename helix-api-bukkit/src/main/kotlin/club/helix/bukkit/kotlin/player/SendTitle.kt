package club.helix.bukkit.kotlin.player

import club.helix.bukkit.util.ReflectionUtil
import org.bukkit.entity.Player
import java.lang.reflect.Constructor

private fun getTitlePacket(type: String, text: String, fadeIn: Int, stay: Int, fadeOut: Int): Any {
    val e = ReflectionUtil.getNMSClass("PacketPlayOutTitle").declaredClasses.first().getField(type).get(null)
    val chatTitle: Any = ReflectionUtil.getNMSClass("IChatBaseComponent").declaredClasses.first().getMethod("a", String::class.java).invoke(null, "{'text': '$text'}")
    val subtitleConstructor: Constructor<*> = ReflectionUtil.getNMSClass("PacketPlayOutTitle").getConstructor(
        ReflectionUtil.getNMSClass("PacketPlayOutTitle").declaredClasses.first(), ReflectionUtil.getNMSClass("IChatBaseComponent"), Int::class.java, Int::class.java, Int::class.java) as Constructor<*>
    return subtitleConstructor.newInstance(e, chatTitle, fadeIn, stay, fadeOut)
}

private fun send(player: Player, fadeIn: Int, stay: Int, fadeOut: Int, title: String, subTitle: String) {
    val fadeInFinal = fadeIn * 20
    val stayFinal  = stay * 20
    val fadeOutFinal = fadeOut * 5

    ReflectionUtil.sendPacket(player, getTitlePacket("TIMES", title, fadeInFinal, stayFinal, fadeOutFinal))
    ReflectionUtil.sendPacket(player, getTitlePacket("TITLE", title, fadeIn, stayFinal, fadeOutFinal))

    if (subTitle.isNotEmpty()) {
        ReflectionUtil.sendPacket(player, getTitlePacket("TIMES", subTitle, fadeInFinal, stayFinal, fadeOutFinal))
        ReflectionUtil.sendPacket(player, getTitlePacket("SUBTITLE", subTitle, fadeInFinal, stayFinal, fadeOutFinal))
    }
}

fun Player.sendPlayerTitle(title: String, subTitle: String, seconds: Int = 2) =
    send(this, 1, seconds, 1, title, subTitle)