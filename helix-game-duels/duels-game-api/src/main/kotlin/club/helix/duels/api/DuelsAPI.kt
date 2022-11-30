package club.helix.duels.api

import club.helix.components.HelixComponents
import club.helix.duels.api.event.GameCreatedEvent
import club.helix.duels.api.event.GameDeleteEvent
import club.helix.duels.api.game.DuelsGame
import club.helix.duels.api.listener.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

abstract class DuelsAPI<T: DuelsGame>(
    private val plugin: JavaPlugin,
    val components: HelixComponents,
) {
    val games = mutableListOf<T>()
    val requestController = DuelsRequestController(components)

    fun registerGame(game: T) = game.apply {
        games.add(this)
        Bukkit.getPluginManager().callEvent(GameCreatedEvent(this))
    }

    abstract fun newGame(): T

    fun onEnable() {
        loadListeners()
    }

    private fun loadListeners() = Bukkit.getPluginManager().run {
        registerEvents(SpectatorPlayerListener(this@DuelsAPI), plugin)
        registerEvents(UserDeathListener(this@DuelsAPI), plugin)
        registerEvents(UserSearchGameListener(this@DuelsAPI), plugin)
        registerEvents(UserDeathListener(this@DuelsAPI), plugin)
        registerEvents(UserGameLeaveListener(this@DuelsAPI), plugin)
        registerEvents(PreGameDamageListener(this@DuelsAPI), plugin)
        registerEvents(GameChatLevelListener(this@DuelsAPI), plugin)
        registerEvents(PlayersItemListener(this@DuelsAPI), plugin)
        registerEvents(PlayAgainItemListener(this@DuelsAPI), plugin)
        registerEvents(StartingTimerListener(), plugin)
        registerEvents(SpectatorJoinGameListener(), plugin)
        registerEvents(CancelPlayerTagHandleListener(), plugin)
        registerEvents(BackLobbyItemListener(), plugin)
        registerEvents(ServerEssentialsListener(), plugin)
        registerEvents(GamePlayersVisibleListener(), plugin)
        registerEvents(UserJoinGameListener(), plugin)
        registerEvents(GameStartListener(), plugin)
        registerEvents(GameDeleteListener(), plugin)
        registerEvents(GameEndListener(this@DuelsAPI), plugin)
        registerEvents(UserJoinListener(), plugin)
    }

    fun deleteGame(game: DuelsGame) {
        games.remove(game)
        Bukkit.getPluginManager().callEvent(GameDeleteEvent(game))
    }

    fun findAvailableGame(): T? = games.firstOrNull { it.available() }

    fun findGame(player: Player) = games.firstOrNull {
        it.allPlayers().any { duelsPlayer -> duelsPlayer.player.name == player.name } }
}