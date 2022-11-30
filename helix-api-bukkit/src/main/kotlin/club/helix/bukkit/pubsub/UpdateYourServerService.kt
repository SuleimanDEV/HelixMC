package club.helix.bukkit.pubsub

import club.helix.bukkit.HelixBukkit
import club.helix.components.pubsub.PubSubService

class UpdateYourServerService(private val apiBukkit: HelixBukkit): PubSubService("update-your-server") {

    override fun onMessage(message: String) =
        apiBukkit.components.callUpdateServer(apiBukkit.currentServer)
}