package club.helix.components.account.controller

import club.helix.components.HelixComponents
import club.helix.components.account.HelixReport
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.util.concurrent.TimeUnit

class ReportController(val components: HelixComponents) {

    fun save(report: HelixReport) = components.redisPool.resource.use {
        val remaingSeconds = TimeUnit.MILLISECONDS.toSeconds(report.remaingTime)
        it.set("reports:${report.accused}", HelixComponents.JSON.encodeToString(report))
        it.expireAt("reports:${report.accused}", remaingSeconds)
    }

    fun load(victim: String): HelixReport? = components.redisPool.resource.use {
        it.takeIf { it.exists("reports:$victim") }?.run {
            val data = if (it.exists("reports:$victim")) it.get("reports:$victim") else return null
            HelixComponents.JSON.decodeFromString<HelixReport>(data)
        }
    }

    fun load() = components.redisPool.resource.use {
        it.keys("reports:*").mapNotNull { key ->
            HelixComponents.JSON.decodeFromString<HelixReport>(it.get(key))
        }
    }
}