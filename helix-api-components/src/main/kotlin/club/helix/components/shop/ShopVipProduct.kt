package club.helix.components.shop

import club.helix.components.account.HelixRank
import club.helix.components.account.HelixRankLife
import club.helix.components.account.HelixUser
import java.util.concurrent.TimeUnit

class ShopVipProduct(
    val id: Int,
    val player: String,
    val product: String,
    amount: Int,
    val server: String,
    val subServer: String,
    group: String,
    days: Int,
    val code: String,
) {
    val rank: HelixRank = when(group.lowercase()) {
        "beta", "betamensal" -> HelixRank.BETA
        "ultra", "ultramensal" -> HelixRank.ULTRA
        "vip", "vipmensal" -> HelixRank.VIP
        else -> throw NullPointerException("invalid rank")
    }

    val time = days * amount
    val permanent = (days == 9999)

    fun getTimeFormatted(): String = if (!permanent)
        "$time ${if (time > 1) "dias" else "dia"}" else
            "Permanente"

    fun apply(user: HelixUser) = user.addRank(HelixRankLife(
        rank, if (permanent) 0 else TimeUnit.DAYS.toMillis(time.toLong())
    ))
}
