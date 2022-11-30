package club.helix.components.clan.controller

import club.helix.components.HelixComponents
import club.helix.components.clan.Clan
import club.helix.components.clan.ClanManager

class ClanSqlController(
    private val clanManager: ClanManager
) {
    private val adapter = HelixComponents.MOSHI.adapter(Clan::class.java)

    fun save(clan: Clan) = clanManager.api.storage.newConnection.use {
        val data = adapter.toJson(clan)
        val insert = "insert into clans (name, data) values ('${clan.name}', '$data')"
        val update = "update clans set data = '$data' where name = '${clan.name}'"
        val rs = it.query("select * from clans where name = '${clan.name}'")
            ?: throw NullPointerException("invalid clan query (SAVE)")

        clanManager.callUpdate(clan)
        it.execute(if (rs.next()) update else insert)
    }

    fun delete(clan: Clan) = clanManager.api.storage.newConnection.use {
        it.execute("delete from clans where name = '${clan.name}'")
    }

    fun loadByMember(name: String) = clanManager.api.storage.newConnection.use {
        it.query("select * from clans where json_contains(data, '{\"name\":\"$name\"}', '$.members')")?.takeIf { query ->
            query.next()
        }?.run {
            adapter.fromJson(getString("data"))
        }
    }

    fun loadByTag(tag: String) = clanManager.api.storage.newConnection.use {
        it.query("select * from clans where json_extract(data, '$.clanTag') = '$tag'")?.takeIf { query ->
            query.next()
        }?.run {
            adapter.fromJson(getString("data"))
        }
    }

    fun loadByName(name: String) = clanManager.api.storage.newConnection.use {
        it.query("select * from clans where name = '$name'")?.takeIf { query ->
            query.next()
        }?.run {
            adapter.fromJson(getString("data"))
        }
    }
}