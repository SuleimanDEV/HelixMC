package club.helix.components.account

import club.helix.components.account.game.Duels
import club.helix.components.account.game.HardcoreGames
import club.helix.components.account.game.PvP
import kotlinx.serialization.Serializable
import java.util.concurrent.TimeUnit

@Serializable
class HelixUser(
    val name: String,
    val ranksLife: MutableList<HelixRankLife> = mutableListOf(HelixRankLife(HelixRank.DEFAULT)),
    var medal: HelixMedal = HelixMedal.DEFAULT,
    var login: HelixUserLogin = HelixUserLogin(),
    val preferences: HelixUserPreference = HelixUserPreference(),
    val hg: HardcoreGames = HardcoreGames(),
    val duels: Duels = Duels(),
    val pvp: PvP = PvP()
) {
    var onlineTime: Long = 0L
    var vanish: Boolean = false

    var tag: HelixRank = HelixRank.DEFAULT
    val mainRankLife get() = ranksLife.minByOrNull { it.rank.ordinal }!!

    fun addRank(rankLife: HelixRankLife, replace: Boolean = false) {
        if (!replace && ranksLife.any { it.rank == rankLife.rank }) {
            throw Exception("this ranks already added!")
        }

        if (replace) ranksLife.removeIf { it.rank == rankLife.rank }
        this.tag = rankLife.rank
        callRankChanged()
        ranksLife.add(rankLife)
    }

    fun addRank(rank: HelixRank, time: Int, timeUnit: TimeUnit, replace: Boolean = false) =
        addRank(HelixRankLife(rank, System.currentTimeMillis() + timeUnit.toMillis(time.toLong())), replace)

    fun addRankPermanent(rank: HelixRank, replace: Boolean = false) = addRank(HelixRankLife(rank), replace)

    fun removeRank(rank: HelixRank) {
        if (rank == HelixRank.DEFAULT) {
            throw Exception("cannot remove default rank")
        }
        if (!ranksLife.any { it.rank == rank }) {
            throw Exception("this user does not have this rank")
        }
        ranksLife.removeIf { it.rank == rank }
        callRankChanged()
        this.tag = mainRankLife.rank
    }

    fun hasRank(rank: HelixRank) = ranksLife.any { it.rank == rank }

    fun getRankLife(rank: HelixRank) = ranksLife.firstOrNull { it.rank == rank }

    fun availableTags() = HelixRank.values().filter {
        it == HelixRank.DEFAULT || hasPermission("helix.tag.${it.toString().lowercase()}")
    }

    fun availableMedals() = HelixMedal.values().filter {
        it == HelixMedal.DEFAULT || hasPermission("helix.medal.${it.toString().lowercase()}")
                || hasPermission("helix.medal.*")
    }

    fun hasPermission(permission: String) = ranksLife.any { it.rank.hasPermission(permission) }

    val permanentRank get() = mainRankLife.time == 0L

    fun setRankPermanent(rank: HelixRank) {
        ranksLife.removeIf { it.rank != HelixRank.DEFAULT }
        addRank(HelixRankLife(rank), true)
        this.tag = rank
        callRankChanged()
    }

    fun setRankTime(rank: HelixRank, time: Long, timeUnit: TimeUnit) {
        ranksLife.removeIf { it.rank != HelixRank.DEFAULT }
        addRank(HelixRankLife(rank,
            timeUnit.toMillis(time).plus(TimeUnit.SECONDS.toMillis(1))
                .plus(System.currentTimeMillis())
        ), true)
        this.tag = rank
        callRankChanged()
    }

    private fun callRankChanged() {
        if (!availableMedals().contains(medal)) {
            medal = HelixMedal.DEFAULT
        }
    }
}