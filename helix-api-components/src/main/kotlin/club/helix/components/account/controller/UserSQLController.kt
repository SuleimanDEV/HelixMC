package club.helix.components.account.controller

import club.helix.components.HelixComponents
import club.helix.components.account.HelixUser
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class UserSQLController(api: HelixComponents) : UserController(api) {

    override fun load(name: String): HelixUser? = api.storage.newConnection.use { it ->
        it.query("select * from accounts where name = '$name'")?.takeIf { it.next() }?.let { query ->
            HelixComponents.JSON.decodeFromString<HelixUser>(query.getString("data"))
        }
    }

    override fun delete(name: String): Unit = api.storage.newConnection.use {
        it.execute("delete from accounts where name = '$name'")
    }

    override fun save(user: HelixUser): Unit = api.executorService.execute {
        api.storage.newConnection.use {
            val data = HelixComponents.JSON.encodeToString(user)

            val resultSet = it.query("select * from accounts where name = '${user.name}'")
                ?: throw NullPointerException("erro ao procurar este jogador")

            val update = "update accounts set data = '$data' where name = '${user.name}'"
            val insert = "insert into accounts (name, data) values ('${user.name}', '$data')"
            it.execute(if (resultSet.next()) update else insert)
        }
    }
}