package club.helix.pvp.fps.vip

import club.helix.components.account.HelixRank
import java.util.*

class CoinsMultiplier(
    private val rank: HelixRank,
    private val minCoins: Int = 5,
    private val maxCoins: Int = 10
) {
    val boost get() = when {
        rank.isBiggerThen(HelixRank.BETA) -> 4.0
        rank == HelixRank.ULTRA -> 3.0
        rank == HelixRank.PRO -> 2.5
        rank == HelixRank.VIP -> 2.0
        rank == HelixRank.BLADE -> 1.5
        else -> 1.0
    }

    fun calcule(): Int {
        val random = Random().nextInt(maxCoins + 1 - minCoins) + minCoins
        return (random * boost).toInt()
    }
}