package club.helix.hologram

class HologramMetaData {
    val datas = mutableMapOf<String, Any>()

    fun set(key: String, value: Any) = datas.put(key, value)

    inline fun <reified T: Any> get(key: String) = datas[key] as? T

    fun has(key: String) = datas.containsKey(key)

    fun has(key: String, value: Any) = datas.any {
        it.key == key && it.value == value
    }
}