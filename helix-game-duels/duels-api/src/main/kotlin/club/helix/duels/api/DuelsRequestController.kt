package club.helix.duels.api

import club.helix.components.HelixComponents

class DuelsRequestController(private val components: HelixComponents) {
    companion object {
        private val jsonAdapter = HelixComponents.MOSHI.adapter(DuelsRequest::class.java)
    }

    fun save(request: DuelsRequest) = components.redisPool.resource.use {
        it.set("duels-request:${request.player}", jsonAdapter.toJson(request))
    }

    fun load(player: String): DuelsRequest? = components.redisPool.resource.use {
        it.get("duels-request:$player")?.let { response -> jsonAdapter.fromJson(response) }
    }

    fun delete(player: String) = components.redisPool.resource.use {
        it.del("duels-request:$player")
    }
}