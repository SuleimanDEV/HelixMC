package club.helix.components.account

import club.helix.components.HelixComponents
import club.helix.components.account.controller.UserRedisController
import club.helix.components.account.controller.UserSQLController

class UserManager(val components: HelixComponents) {
    val users = mutableMapOf<String, HelixUser>()

    val userSqlController = UserSQLController(components)
    val redisController = UserRedisController(components)

    fun getUser(name: String): HelixUser? = users[name]

    fun registered(name: String) = userSqlController.load(name) != null

    fun removeUser(user: HelixUser) = users.remove(user.name)
    fun removeUser(name: String) = users.remove(name)

    fun putUser(user: HelixUser) = users.put(user.name, user)

    fun load(name: String) = getUser(name)
        ?: redisController.load(name)
        ?: userSqlController.load(name)

    fun saveAll(user: HelixUser, callUpdate: Boolean = true) {
        redisController.save(user)
        userSqlController.save(user)
        if (callUpdate) callUpdate(user)
    }

    private fun callUpdate(user: HelixUser) = components.redisPool.resource.use {
        it.publish("user-update", HelixComponents.MOSHI
            .adapter(HelixUser::class.java).toJson(user))
    }
}