package club.helix.pvp.fps.util

import java.util.concurrent.TimeUnit

class CombatLog {
    companion object {
        private val players = mutableMapOf<String, Long>()
    }

    fun set(name: String, seconds: Int = 10) =
        players.put(name, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds.toLong()))

    fun remove(name: String) = players.remove(name)

    fun contains(name: String) = players.any {
        it.key == name && it.value >= System.currentTimeMillis() }

}