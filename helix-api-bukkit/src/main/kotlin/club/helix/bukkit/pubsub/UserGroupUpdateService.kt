package club.helix.bukkit.pubsub

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.nms.NameTagNMS
import club.helix.components.HelixComponents
import club.helix.components.account.HelixRankLife
import club.helix.components.account.HelixUser
import club.helix.components.pubsub.PubSubService
import com.google.gson.JsonObject
import org.bukkit.Bukkit

class UserGroupUpdateService(private val apiBukkit: HelixBukkit): PubSubService("user-group-update") {

    override fun onMessage(message: String): Unit = HelixComponents.GSON.run {
        val json = fromJson(message, JsonObject::class.java)
            ?: throw NullPointerException("ocorreu um erro deserializar o json")

        val type = json.get("type").asString

        val user = fromJson(json.get("user").asString, HelixUser::class.java)
            ?: throw NullPointerException("ocorreu um erro ao deserializar o user")

        val rankLife = fromJson(json.get("rank-life").asString, HelixRankLife::class.java)
            ?: throw NullPointerException("invalid rank-life")

        val player = Bukkit.getPlayer(user.name)?.apply {
            apiBukkit.components.userManager.putUser(user)
            apiBukkit.permissionManager.recalculate(this)
            NameTagNMS.setNametag(this, user.tag, NameTagNMS.Reason.TAG)
        } ?: return@run

        when (type.uppercase()) {
            "ADD", "SET" -> {
                player.sendMessage(arrayOf(
                    "§aVocê recebeu o rank ${rankLife.rank.coloredName}§a.",
                    "§aDuração: ${rankLife.formatTime()}"
                ))
            }
            "REMOVE" -> {
                player.sendMessage("§aSeu rank ${rankLife.rank.displayName} foi removido.")
            }
        }
    }
}