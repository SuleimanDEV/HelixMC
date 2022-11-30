package club.helix.components.pubsub

import redis.clients.jedis.JedisPubSub

class PubSubListener(private val pubSubManager: PubSubManager): JedisPubSub() {

    override fun onMessage(channel: String, message: String) {
        println("[PubSub] Receiving from $channel: ${message.takeIf { it.length <= 55 } ?: "${message.substring(0, 52)}..."}")
        pubSubManager.get(channel)?.onMessage(message)
    }
}