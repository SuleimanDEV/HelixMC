package club.helix.bukkit.util

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.pubsub.UpdateClanService
import club.helix.bukkit.pubsub.UpdateYourServerService
import club.helix.bukkit.pubsub.UserActiveVipService
import club.helix.bukkit.pubsub.UserGroupUpdateService

class PubSubRegister(private val apiBukkit: HelixBukkit) {

    fun register() = apiBukkit.components.pubsubManager.register(
        UpdateYourServerService(apiBukkit),
        UserGroupUpdateService(apiBukkit),
        UpdateClanService(apiBukkit.components),
        UserActiveVipService(apiBukkit.components)
    )
}