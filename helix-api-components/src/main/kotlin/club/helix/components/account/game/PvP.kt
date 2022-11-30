package club.helix.components.account.game

import kotlinx.serialization.Serializable
import java.util.*
import kotlin.math.abs

@Serializable
data class PvP(
    val arena: PvPStats = PvPStats(),
    val fps: PvPStats = PvPStats(),
    var coins: Int = 0,
    var converted: Boolean = false
) {
    companion object {
        const val ADD_XP_ON_UP = 30
    }

    var rank = Rank.SOLDIER
        private set
    var subrank: Int = rank.subranks.first
        private set
    var expToUp = 450
        private set
    var exp: Int = 0
        private set

    fun romanNumeral(subrank: Int): String = when(subrank) {
        1 -> "I"
        2 -> "II"
        3 -> "III"
        4 -> "IV"
        5 -> "V"
        else -> "?"
    }
    fun displayRank() = "${rank.color}${romanNumeral(subrank)}${rank.symbol}"

    fun addExp(exp: Int): LevelResponse {
        var response: LevelResponse

        try {
            this.exp += exp
            val levelup = this.exp >= expToUp
            response = if (levelup) LevelResponse.LEVEL_UP else LevelResponse.SUCCESS

            nextRank()?.takeIf { levelup }?.let {
                this.expToUp += ADD_XP_ON_UP
                this.exp = 0
                this.rank = it.key
                this.subrank = it.value
            }
        }catch (error: Exception) {
            response =  LevelResponse.FAILED
            error.printStackTrace()
        }
        return response
    }

    fun removeExp(exp: Int): LevelResponse {
        var response: LevelResponse

        try {
            val leveldown = this.exp != 0 && (this.exp - exp) < 0
            response = if (leveldown) LevelResponse.LEVEL_DOWN else LevelResponse.SUCCESS

            previousRank()?.takeIf { leveldown }?.let {
                this.expToUp -= 30
                this.exp = expToUp.rem(abs(this.exp - exp))
                this.rank = it.key
                this.subrank = it.value
            } ?: run {
                this.exp -= exp
                if (this.exp < 0) this.exp = 0
            }
        }catch (error: Exception) {
            response =  LevelResponse.FAILED
            error.printStackTrace()
        }
        return response
    }

    fun nextRank(): AbstractMap.SimpleEntry<Rank, Int>? {
        val nextRank = rank.takeIf {
            it.subranks.contains(subrank.inc()) || Rank.values().size < rank.ordinal.plus(2)
        } ?: Rank.values()[rank.ordinal.inc()]

        if (nextRank == rank && !rank.subranks.contains(subrank.inc())) return null
        val nextSubRank = if (nextRank == rank) subrank.inc() else nextRank.subranks.first

        return AbstractMap.SimpleEntry(nextRank, nextSubRank)
    }

    private fun previousRank(): AbstractMap.SimpleEntry<Rank, Int>? {
        val previousRank = rank.takeIf {
            it.subranks.contains(subrank.dec()) || rank.ordinal < Rank.values().size
        } ?: Rank.values()[rank.ordinal.dec()]

        if (previousRank == rank && !rank.subranks.contains(subrank.dec())) return null
        val previousSubRank = if (previousRank == rank) subrank.dec() else previousRank.subranks.last

        return AbstractMap.SimpleEntry(previousRank, previousSubRank)
    }

    enum class LevelResponse {
        SUCCESS, FAILED, LEVEL_UP, LEVEL_DOWN;
    }

    enum class Rank(
        val displayName: String,
        val color: String,
        val symbol: String,
        val subranks: IntRange
    ) {

        SOLDIER("Soldado", "§a", "☆", 1..5),

        SPECIALIST("Especialista", "§9", "✦", 1..5),

        SARGEANT("Sargento", "§6", "❉", 1..5),

        CAPTAIN("Capitão", "§d", "✥", 1..5),

        MAJOR("Major", "§8", "❈", 1..5),

        LIEUTENANT("Tenente", "§c", "❂", 1..5),

        COLONEL("Coronel", "§d", "✪", 1..5),

        WAR_HERO("Herói de Guerra", "§2", "➹", 1..5),

        LEGENDARY("Lendário", "§c", "∞", 1..1);

        fun isLast() = values().size >= this.ordinal.inc()
    }
}

@Serializable
data class PvPStats(
    var kills: Int = 0,
    var deaths: Int = 0,
    var killstreak: Int = 0,
    var maxKillStreak: Int = 0
)


