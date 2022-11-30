package club.helix.lobby.provider.collectible.controller

import club.helix.lobby.provider.collectible.Collectible
import club.helix.lobby.provider.collectible.CollectibleCategory
import club.helix.lobby.provider.collectible.CollectibleManager
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.entity.Player

class CollectibleUserController(private val collectibleManager: CollectibleManager) {

    init {
        collectibleManager.plugin.apiBukkit.components.storage.newConnection.use {
            it.execute("create table if not exists collectibles (ID int not null auto_increment, " +
                        "user varchar(40) not null, " +
                    "activeCollectibles json not null, " +
                    "primary key (ID))")
        }
    }

    fun save(player: Player, activeCollectibles: MutableList<Collectible>) = collectibleManager.plugin.apiBukkit.components.storage.newConnection.use { connection ->
        val serialize = Json.encodeToString(activeCollectibles.map(Collectible::id).toMutableList())

        val insert = "insert into collectibles (user, activeCollectibles) values ('${player.name}', '$serialize')"
        val update = "update collectibles set activeCollectibles = '$serialize' where user = '${player.name}'"

        val query = connection.query("select * from collectibles where user = '${player.name}'")
            ?: throw NullPointerException("invalid query")
        connection.execute(if (query.next()) update else insert)
    }

    fun delete(player: Player) = collectibleManager.plugin.apiBukkit.components.storage.newConnection.use { connection ->
        connection.execute("delete from collectibles where user = '${player.name}'")
    }

    fun load(player: Player): MutableList<Collectible>? = collectibleManager.plugin.apiBukkit.components.storage.newConnection.use { connection ->
        connection.query("select * from collectibles where user = '${player.name}'")?.takeIf {
            it.next()
        }?.let {
            val serialized = it.getString("activeCollectibles")
            val activeCollectible = Json.decodeFromString<MutableList<String>>(serialized)

            mutableListOf<Collectible>().apply {
                addAll(activeCollectible.mapNotNull { id -> collectibleManager.getCollectible(id) })
            }
        }
    }

    @Serializable
    private class ActiveCollectible(
        val id: String,
        val category: CollectibleCategory
    )
}