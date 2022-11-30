package club.helix.bungeecord.pubsub

import club.helix.components.HelixComponents
import club.helix.components.account.HelixUser
import club.helix.components.pubsub.PubSubService
import com.google.gson.JsonObject
import net.md_5.bungee.api.ProxyServer

class UserGroupUpdateService: PubSubService("user-group-update") {

    override fun onMessage(message: String): Unit = HelixComponents.GSON.run {
        val json = fromJson(message, JsonObject::class.java)
            ?: throw NullPointerException("ocorreu um erro ao deserializar o json")

        val user = fromJson(json.get("user").asString, HelixUser::class.java)
            ?: throw NullPointerException("Ocorreu um erro ao deserializar o user")

        ProxyServer.getInstance().getPlayer(user.name)?.apply {
            val permissions = mutableListOf<String>().apply {
                user.ranksLife.forEach { addAll(it.rank.permissions) }
            }
            permissions.forEach { setPermission(it, true) }
            println("${user.name} PERMISSÕES ATUALIZADAS!!!")
        }
    }
}