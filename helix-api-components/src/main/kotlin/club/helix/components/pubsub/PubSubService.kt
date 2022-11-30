package club.helix.components.pubsub

abstract class PubSubService(val channel: String) {
    abstract fun onMessage(message: String)
}