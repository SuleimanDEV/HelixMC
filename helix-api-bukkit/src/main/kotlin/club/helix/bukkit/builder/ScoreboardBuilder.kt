package club.helix.bukkit.builder

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot

open class ScoreboardBuilder(
    private val displayName: String,
    private val objectiveName: String
) {
    private val lines = mutableMapOf<String, String>()
    private val emptyLines = mutableListOf("§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a",
        "§b", "§c", "§d", "§e", "§f")

    fun addLine(text: String, teamName: String = "") = lines.put(text.takeIf { it.isNotEmpty() }
        ?: (emptyLines.filter { !lines.containsKey(it) }.toMutableList().removeAt(0)), teamName)

    fun clearLines() = lines.clear()

    open fun build(player: Player) {
        val scoreboard = Bukkit.getScoreboardManager().newScoreboard
        val objective = scoreboard.registerNewObjective(objectiveName, "dummy")

        objective.displaySlot = DisplaySlot.SIDEBAR
        objective.displayName = displayName

        var index = lines.size - 1
        lines.entries.forEach {
            it.value.takeIf { value -> value.isNotEmpty() }?.apply {
                scoreboard.registerNewTeam(it.value).addEntry(it.key)
            }

            objective.getScore(it.key).score = index--
        }

        player.scoreboard = scoreboard
        update(player)
    }

    fun startUpdater(plugin: JavaPlugin, seconds: Int = 3) = Bukkit.getScheduler().runTaskTimer(plugin, {
        plugin.server.onlinePlayers.filter { it.scoreboard.getObjective(objectiveName) != null }.forEach(this::update)
    }, 0L, seconds * 20L)

    fun startAsyncUpdater(plugin: JavaPlugin, seconds: Int = 3) = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, {
        plugin.server.onlinePlayers.filter { it.scoreboard.getObjective(objectiveName) != null }.forEach(this::update)
    }, 0L, seconds * 20L)

    open fun update(player: Player) {}
}