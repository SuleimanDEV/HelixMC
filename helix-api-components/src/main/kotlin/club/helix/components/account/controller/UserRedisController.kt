package club.helix.components.account.controller

import club.helix.components.HelixComponents
import club.helix.components.account.HelixUser
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class UserRedisController(api: HelixComponents): UserController(api) {

    override fun load(name: String): HelixUser? = api.redisPool.resource.use {
        it.get("account:$name")?.let { jsonText ->
            HelixComponents.JSON.decodeFromString<HelixUser>(jsonText) }
    }

    override fun delete(name: String): Unit = api.redisPool.resource.use {
        it.del("account:$name")
    }

    override fun save(user: HelixUser): Unit = api.redisPool.resource.use {
        it.set("account:${user.name}", HelixComponents.JSON.encodeToString(user))
    }
}