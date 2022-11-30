package club.helix.components.account.game

import java.util.*

class DuelsLevel(val level: Int) {
    companion object {
        private val options = mutableListOf<AbstractMap.SimpleEntry<String, String>>(
            AbstractMap.SimpleEntry("§7", "✯"),
            AbstractMap.SimpleEntry("§a", "✦"),
            AbstractMap.SimpleEntry("§e", "✿"),
            AbstractMap.SimpleEntry("§6", "✤"),
            AbstractMap.SimpleEntry("§d", "✪"),
            AbstractMap.SimpleEntry("§b", "❉"),
            AbstractMap.SimpleEntry("§3", "❂"),
            AbstractMap.SimpleEntry("§5", "☣"),
            AbstractMap.SimpleEntry("§8", "☯"),
            AbstractMap.SimpleEntry("§c", "☬"),
            AbstractMap.SimpleEntry("§4", "☠"),
        )
    }

    fun color(): String {
        val index = (level.toDouble() / 10).toInt()
        return if (index < options.size)
            options[index].key else "§0"
    }

    fun symbol(): String {
        val index = (level.toDouble() / 10).toInt()
        return if (index < options.size)
            options[index].value else "X"
    }
}