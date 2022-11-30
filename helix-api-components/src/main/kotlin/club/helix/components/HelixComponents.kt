package club.helix.components

import club.helix.components.account.UserManager
import club.helix.components.account.controller.ReportController
import club.helix.components.account.permission.PermissionLoader
import club.helix.components.account.permission.PermissionManager
import club.helix.components.clan.ClanManager
import club.helix.components.pubsub.PubSubManager
import club.helix.components.pubsub.provider.ServerUpdateService
import club.helix.components.server.HelixServerProvider
import club.helix.components.storage.provider.MySQL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HelixComponents(permissionLoader: PermissionLoader) {
    companion object {
        val GSON: Gson = GsonBuilder().create()
        val MOSHI: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory())
            .build()

        val JSON = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }

    val executorService: ExecutorService = Executors.newCachedThreadPool()
    val storage: MySQL = MySQL("localhost", "root", "Amzin309a()($*_AJMZI(#178azM$(*A*(#@(09aMijh$0*U$8aMNnzun#iaj099", "server", 3306)

    val redisPool: JedisPool = JedisPool(JedisPoolConfig().apply {
        maxIdle = 65
        maxTotal = maxIdle

    }, "localhost", 6379, 1800, "KKOJ)(88a9089mzni9#*)aoiMZIHN#0a90-Z<MI)Oja398i0-aiK)IZ")

    val userManager = UserManager(this)
    val permissionManager = PermissionManager(permissionLoader)
    val clanManager = ClanManager(this)
    val reportController = ReportController(this)
    val pubsubManager get() = PubSubManager(this)

    fun updateServers() = redisPool.resource.use { it.publish("update-your-server", "") }

    fun callUpdateServer(server: HelixServerProvider) = executorService.execute {
        redisPool.resource.use { it.publish("update-server", JSON.encodeToString(server)) }
    }

    fun start() {
        permissionManager.permissionLoader.load()
        executorService.execute { storage.createTables() }
        pubsubManager.register(ServerUpdateService())
    }

    fun close() {
        permissionManager.permissionLoader.unloadFile()
        redisPool.close()
        redisPool.destroy()
        executorService.shutdown()
    }
}