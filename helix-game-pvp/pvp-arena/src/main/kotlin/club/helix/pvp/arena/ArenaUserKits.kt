package club.helix.pvp.arena

import club.helix.components.HelixComponents
import club.helix.pvp.arena.kit.ArenaKit

class ArenaUserKits(private val components: HelixComponents) {
    companion object {
        private val userKits = mutableMapOf<String, MutableList<ArenaKit>>()
    }

    fun getKits(name: String) = userKits[name]
    fun hasKit(name: String, kit: ArenaKit) = getKits(name)?.contains(kit) == true
    fun addKit(name: String, kit: ArenaKit) = userKits.put(name, getKits(name)?.apply { add(kit) } ?: mutableListOf(kit))
    fun unregister(name: String) = userKits.remove(name)

    fun load(name: String): Unit = components.storage.newConnection.use { storage ->
        storage.query("select * from arena_user_kits where name = '$name'")?.takeIf { it.next() }?.let { query ->
            userKits[name] = HelixComponents.GSON.fromJson(query.getString("data"), mutableListOf<String>()::class.java)
                .map { ArenaKit.valueOf(it) }.toMutableList()
        }
    }

    fun save(name: String) = components.storage.newConnection.use {
        val resultSet = it.query("select * from arena_user_kits where name = '$name'")
            ?: throw NullPointerException("não foi possível encontrar os kits deste jogador")
        val kits = getKits(name) ?: throw NullPointerException("invalid kit list")
        val data =  HelixComponents.GSON.toJson(kits) ?: throw NullPointerException("invalid json object")

        val insert = "insert into arena_user_kits (name, data) values ('$name', '$data')"
        val update = "update arena_user_kits set data = '$data' where name = '$name'"
        it.execute(if (resultSet.next()) update else insert)
    }

    fun createTableIfNotExists() = components.storage.newConnection.use {
        it.execute("create table if not exists arena_user_kits (" +
                "ID int not null auto_increment, name varchar(40), " +
                "data json, primary key (ID))")
    }
}