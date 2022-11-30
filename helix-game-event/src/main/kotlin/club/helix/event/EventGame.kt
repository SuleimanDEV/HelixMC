package club.helix.event

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.event.event.GameConfigChangeEvent
import club.helix.event.event.GameStateChangeEvent
import club.helix.event.player.GamePlayer
import club.helix.event.player.GamePlayerType
import club.helix.event.player.event.GamePlayerJoinEvent
import club.helix.event.scoreboard.WhitelistScoreboard
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit

class EventGame {

    var state: State = State.WHITELIST
        private set
    var time: Int = 0

    var scoreboard: ScoreboardBuilder = WhitelistScoreboard(this)
        private set
    val players = mutableListOf<GamePlayer>()
    val configurations = mutableMapOf<String, Any>()
    val blocks = mutableListOf<Block>()

    @JvmName("getCastConfig")
    inline fun <reified T: Any> getConfig(key: String) = configurations[key.lowercase()] as? T
    fun getConfig(key: String) = configurations[key.lowercase()]

    fun setConfig(key: String, value: Any) {
        getConfig(key)?.let { convertConfigInstance(it, value) }
        val event = GameConfigChangeEvent(this, key, getConfig(key), value)
        Bukkit.getPluginManager().callEvent(event)
        configurations[key] = value
    }

    fun convertConfigInstance(configValue: Any, instance: Any): Any = when {
        configValue is Boolean && instance.toString().toBooleanStrictOrNull() != null -> instance.toString().toBoolean()
        configValue is Int && instance.toString().toIntOrNull() != null -> instance.toString().toInt()
        configValue is String && instance is String -> instance
        else -> throw NullPointerException("invalid value instance")
    }

    fun putPlayer(player: Player, type: GamePlayerType) = GamePlayer(player, type).apply {
        players.removeIf { it.player.name == player.name }
        players.add(this)
        Bukkit.getPluginManager().callEvent(GamePlayerJoinEvent(this, this@EventGame))
    }

    fun removePlayer(player: Player) = players.removeIf { it.player.name == player.name }

    fun playSound(sound: Sound) = players.map(GamePlayer::player)
        .forEach { it.playSound(it.location, sound, 10.0f, 10.0f) }

    fun changeState(state: State) {
        this.state = state
        state.initTime.takeIf { it != -1 }?.let { this.time = it }
        Bukkit.getPluginManager().callEvent(GameStateChangeEvent(this))
    }

    fun changeScoreboard(scoreboard: ScoreboardBuilder) {
        this.scoreboard = scoreboard
        Bukkit.getServer().onlinePlayers.forEach(scoreboard::build)
    }

    fun getPlayer(player: Player, type: GamePlayerType) = players.firstOrNull {
        it.player == player && it.type == type }

    fun hasPlayer(player: Player, type: GamePlayerType) = players.any {
        it.player == player && it.type == type }

    fun getPlayers(type: GamePlayerType) = players.filter { it.type == type }

    fun getTimeFormatted(): String {
        val minutes = time / 60
        val seconds = time % 60
        val minuteFormat = "${if (minutes < 10) "0" else ""}$minutes"
        val secondFormat = "${if (seconds < 10) "0" else ""}$seconds"
        return "$minuteFormat:$secondFormat"
    }

    enum class State(val initTime: Int = 0) {
        WHITELIST,
        WAITING(TimeUnit.MINUTES.toSeconds(5).toInt()),
        PLAYING,
        ENDED(TimeUnit.SECONDS.toSeconds(5).toInt());
    }
}