package club.helix.components.account.game

import kotlinx.serialization.Serializable

@Serializable
class Duels(
    val soup: BasicDuels = BasicDuels(),
    val lava: BasicDuels = BasicDuels(),
    val sumo: BasicDuels = BasicDuels(),
    val gladiator: BasicDuels = BasicDuels(),
    val noDebuff: BasicDuels = BasicDuels(),
    val uhc: BasicDuels = BasicDuels(),
    val combo: BasicDuels = BasicDuels(),
    val gapple: BasicDuels = BasicDuels(),
    val theBridge: TheBridge = TheBridge()
) {
    val expToUp = 1000
    var level: Int = 0
    var exp: Int = 0
        private set

    fun addExp(exp: Int) = if ((this.exp + exp) >= expToUp) {
        this.level++
        this.exp = 0
    }else {
        this.exp += exp
    }
}

@Serializable
class TheBridge(var points: Int = 0): BasicDuels()

@Serializable
open class BasicDuels(
    var matches: Int = 0,
    var wins: Int = 0,
    var defeats: Int = 0,
    var winstreak: Int = 0,
    var maxWinstreak: Int = 0
)