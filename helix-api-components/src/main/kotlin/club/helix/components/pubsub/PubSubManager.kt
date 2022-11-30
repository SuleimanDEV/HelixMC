package club.helix.components.pubsub

import club.helix.components.HelixComponents

class PubSubManager(private val components: HelixComponents) {
    companion object {
        private val services = mutableListOf<PubSubService>()
    }

    private val listener = PubSubListener(this)

    fun get(channel: String) = services.firstOrNull { it.channel.lowercase() == channel.lowercase() }


    fun register(vararg services: PubSubService) {
        Companion.services.addAll(services)

        components.executorService.execute {
            components.redisPool.resource.use {
                it.subscribe(listener, *services.map(PubSubService::channel).toTypedArray())
            }
        }
    }
}