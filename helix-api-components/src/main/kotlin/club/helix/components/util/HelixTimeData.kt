package club.helix.components.util

import java.util.concurrent.TimeUnit

class HelixTimeData {
    companion object {
        private val data = mutableMapOf<String, MutableMap<Any, Long>>()

        fun getTimes(id: String) = data[id]
        fun getActiveTimes(id: String) = getTimes(id)?.filter { isActive(id, it.key) }

        fun setTimes(id: String, times: MutableMap<Any, Long>) = data.put(id, times)

        fun putTime(id: String, key: Any, time: Long, timeUnit: TimeUnit) {
            val values = data[id] ?: mutableMapOf()
            data[id] = values.apply { put(key, System.currentTimeMillis() + timeUnit.toMillis(time)) }
        }

        fun isActive(id: String, key: Any) =
            data[id]?.get(key)?.let { it > System.currentTimeMillis() } == true

        fun getTime(id: String, key: Any): Long? = data[id]?.get(key)

        fun delete(id: String, key: Any) = data[id]?.remove(key)
        fun delete(id: String) = data.remove(id)

        fun getOrCreate(id: String, key: Any, time: Long, timeUnit: TimeUnit): Boolean {
            if (!isActive(id, key)) {
                putTime(id, key, time, timeUnit)
                return false
            }
            return true
        }

        fun getTimeFormatted(id: String, key: Any): String? = getTime(id, key)?.let {
            HelixTimeFormat.format(it, true)
        }
    }
}