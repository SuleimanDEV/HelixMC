package club.helix.hg

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.bukkit.kotlin.player.Build.Companion.build
import club.helix.components.server.provider.HardcoreGamesProvider
import club.helix.hg.player.GamePlayer
import club.helix.hg.player.GamePlayerType
import club.helix.hg.player.event.GamePlayerJoinEvent
import club.helix.hg.player.event.GameStateChangeEvent
import club.helix.hg.scoreboard.WaitingScoreboard
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

class HgGame {

    var state = HardcoreGamesProvider.State.WAITING
        private set
    var time = state.initTime
    var scoreboard: ScoreboardBuilder = WaitingScoreboard(this)
        private set
    val players = mutableListOf<GamePlayer>()
    val maxPlayers = 100

    var build = false
        private set

    fun putPlayer(player: Player, type: GamePlayerType) = GamePlayer(player, type).apply {
        players.removeIf { it.player.name == player.name }
        players.add(this)
        Bukkit.getPluginManager().callEvent(GamePlayerJoinEvent(this, this@HgGame))
    }

    fun playSound(sound: Sound) = players.map(GamePlayer::player)
        .forEach { it.playSound(it.location, sound, 10.0f, 10.0f) }

    fun changeState(state: HardcoreGamesProvider.State) {
        this.state = state
        state.initTime.takeIf { it != -1 }?.let { this.time = it }
        Bukkit.getPluginManager().callEvent(GameStateChangeEvent(this))
    }

    fun changeScoreboard(scoreboard: ScoreboardBuilder) {
        this.scoreboard = scoreboard
        Bukkit.getServer().onlinePlayers.forEach(scoreboard::build)
    }

    fun changeBuild(build: Boolean) {
        this.build = build
        getPlayers(GamePlayerType.PLAYING).map(GamePlayer::player).forEach { it.build(build) }
    }

    fun getPlayers(type: GamePlayerType) = players.filter { it.type == type }

    fun getPlayer(name: String) = players.firstOrNull {
        it.player.name.lowercase() == name.lowercase()
    }

    fun getPlayer(name: String, type: GamePlayerType) = getPlayers(type).firstOrNull {
        it.player.name.lowercase() == name.lowercase()
    }

    fun hasPlayer(name: String, type: GamePlayerType) = getPlayers(type).any {
        it.player.name.lowercase() == name.lowercase()
    }

    fun removePlayer(player: Player) = players.removeIf {
        it.player.name.lowercase() == player.name.lowercase()
    }

    fun getTimeFormatted(): String {
        val minutes = time / 60
        val seconds = time % 60
        val minuteFormat = "${if (minutes < 10) "0" else ""}$minutes"
        val secondFormat = "${if (seconds < 10) "0" else ""}$seconds"
        return "$minuteFormat:$secondFormat"
    }
}