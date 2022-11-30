package club.helix.duels.api

class DuelsRequest(
    val player: String,
    val method: String,
    val options: MutableMap<String, Any> = mutableMapOf()
)