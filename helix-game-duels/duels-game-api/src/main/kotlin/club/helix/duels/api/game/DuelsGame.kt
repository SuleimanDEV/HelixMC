package club.helix.duels.api.game

import club.helix.bukkit.builder.ScoreboardBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.duels.api.event.GameChangeStateEvent
import club.helix.duels.api.event.GameEndEvent
import club.helix.duels.api.event.SpectatorJoinGameEvent
import club.helix.duels.api.event.UserJoinGameEvent
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

abstract class DuelsGame(
    val maxPlayers: Int,
    val mode: String,
    var time: Int = 0,
    val playingPlayers: MutableList<DuelsPlayer> = mutableListOf(),
    val spectatorPlayers: MutableList<Player> = mutableListOf(),
) {
    var state: String = DuelsGameState.WAITING
        private set

    abstract var scoreboard: ScoreboardBuilder

    fun available(): Boolean = state == DuelsGameState.WAITING
            && playingPlayers.size < maxPlayers

    fun end(loser: DuelsPlayer) {
        changeState(DuelsGameState.ENDED)
        time = 6

        val winner = getWinner(loser) ?: throw NullPointerException("invalid winner")
        Bukkit.getPluginManager().callEvent(
            GameEndEvent(
            winner, loser,
            winner.player.account, loser.player.account,
            this)
        )
    }

    fun addPlayer(player: Player) {
        if (playingPlayers.size > 2) throw NullPointerException("maximum players!")
        Bukkit.getOnlinePlayers().forEach {
            it.hidePlayer(player)
            player.hidePlayer(it)
        }

        playingPlayers.map(DuelsPlayer::player).forEach {
            it.showPlayer(player)
            player.showPlayer(it)
        }
        val teamColor = DuelsPlayerColor.values().first { teamColor -> !playingPlayers.any { it.teamColor == teamColor } }
        playingPlayers.add(DuelsPlayer(player, teamColor))
        scoreboard.build(player)
        Bukkit.getPluginManager().callEvent(UserJoinGameEvent(player, this))
    }

    fun addSpectator(player: Player) {
        spectatorPlayers.add(player)
        scoreboard.build(player)
        player.canPickupItems = false
        playingPlayers.map(DuelsPlayer::player).forEach(player::showPlayer)
        Bukkit.getPluginManager().callEvent(
            SpectatorJoinGameEvent(player, this)
        )
    }

    fun changeState(state: String) {
        Bukkit.getPluginManager().callEvent(GameChangeStateEvent(this, state))
        this.state = state
    }

    fun broadcast(vararg messages: String) = allPlayers().forEach {
        it.sendMessage(messages) }

    fun playSound(sound: Sound) = allPlayers().forEach {
        it.playSound(it.location, sound, 1.0f, 1.0f) }

    fun removePlayer(player: Player) {
        playingPlayers.removeIf { it.player.name == player.name}
        spectatorPlayers.remove(player)
    }

    fun changeScoreboard(scoreboard: ScoreboardBuilder) {
        this.scoreboard = scoreboard
        allPlayers().forEach { scoreboard.build(it) }
    }

    fun allPlayers(): MutableList<Player> = mutableListOf<Player>().apply {
        addAll(playingPlayers.map(DuelsPlayer::player)); addAll(spectatorPlayers) }

    fun getPlayingPlayer(player: Player) = playingPlayers.firstOrNull {
        it.player.name == player.name }

    fun getSpectatorPlayer(player: Player) = spectatorPlayers.firstOrNull {
        it.player.name == player.name }

    fun isSpectator(player: Player) = getSpectatorPlayer(player) != null
    fun isPlayingPlayer(player: Player) = getPlayingPlayer(player) != null

    fun getWinner(loser: DuelsPlayer) = playingPlayers.firstOrNull {
        it.player.name != loser.player.name
    }

    fun geTimeFormatted(): String {
        val minutes = time / 60
        val seconds = time % 60
        val minuteFormat = minutes.takeIf { it > 9 } ?: "0$minutes"
        val secondFormat = seconds.takeIf { it > 9 } ?: "0$seconds"
        return "$minuteFormat:$secondFormat"
    }
}