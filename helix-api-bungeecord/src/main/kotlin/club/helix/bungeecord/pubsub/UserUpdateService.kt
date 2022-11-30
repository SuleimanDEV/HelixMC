package club.helix.bungeecord.pubsub

import club.helix.components.HelixComponents
import club.helix.components.account.HelixUser
import club.helix.components.pubsub.PubSubService

class UserUpdateService(private val api: HelixComponents): PubSubService("user-update") {

    override fun onMessage(message: String) {
        val newUser = HelixComponents.MOSHI.adapter(HelixUser::class.java).fromJson(message)
            ?: throw NullPointerException("invalid new user")
        api.userManager.putUser(newUser)
    }
}