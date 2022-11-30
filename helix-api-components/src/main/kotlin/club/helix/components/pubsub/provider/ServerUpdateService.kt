package club.helix.components.pubsub.provider

import club.helix.components.HelixComponents
import club.helix.components.pubsub.PubSubService
import club.helix.components.server.HelixServer
import club.helix.components.server.HelixServerProvider
import kotlinx.serialization.decodeFromString

class ServerUpdateService: PubSubService("update-server") {

    override fun onMessage(message: String) {
        val serverUpdated = HelixComponents.JSON.decodeFromString<HelixServerProvider>(message)

        HelixServer.valueOf(serverUpdated.type.toString()).updateProvider(serverUpdated)
    }
}