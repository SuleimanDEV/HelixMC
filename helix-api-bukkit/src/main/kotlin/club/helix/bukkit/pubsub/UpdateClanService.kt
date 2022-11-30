package club.helix.bukkit.pubsub

import club.helix.components.HelixComponents
import club.helix.components.pubsub.PubSubService

class UpdateClanService(private val components: HelixComponents): PubSubService("update-clan") {

    override fun onMessage(message: String) {
        components.clanManager.takeIf {
            it.containsClanByName(message)
        }?.apply {
            val clanLoaded = controller.loadByName(message) ?: return@apply
            clans[message] = clanLoaded
        }
    }
}