package club.helix.components.account.game

import kotlinx.serialization.Serializable

@Serializable
class HardcoreGames(
    val mix: HardcoreGamesStats = HardcoreGamesStats(),
    val league: HardcoreGamesStats = HardcoreGamesStats(),
    var coins: Int = 0
)

@Serializable
class HardcoreGamesStats(
    var wins: Int = 0,
    var kills: Int = 0,
    var deaths: Int = 0,
    var winstreak: Int = 0,
    var maxWinStreak: Int = 0
)