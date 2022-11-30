package club.helix.components.clan

import club.helix.components.HelixComponents
import club.helix.components.clan.controller.ClanSqlController
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class ClanManager(
    val api: HelixComponents
) {
    val clans = mutableMapOf<String, Clan>()
    val controller = ClanSqlController(this)

    val clanPattern: Pattern = Pattern.compile("[^A-Za-z0-9 ]")
    val invites = mutableMapOf<String, MutableMap<Clan, Long>>()

    fun callUpdate(clan: Clan) = api.redisPool.resource.use {
        it.publish("update-clan", clan.tag)
    }

    fun register(clan: Clan) = clans.put(clan.name, clan)
    fun unregister(clan: Clan) = clans.remove(clan.name)

    fun getClanByName(value: String) = clans[value]
    fun getClanByTag(value: String) = clans.values.firstOrNull {
        it.tag.lowercase() == value.lowercase() }
    fun getClanByMember(name: String) = clans.values.firstOrNull {
        it.getMember(name) != null }
    fun hasMemberClan(player: String) = clans.values.any { it.containsMember(player) }
    fun containsClanByName(name: String) = clans.containsKey(name)

    fun hasInvite(player: String, clan: Clan) = invites[player]?.contains(clan)

    fun createInvite(player: String, clan: Clan, time: Long, timeunit: TimeUnit) {
        val data = (invites[player.lowercase()] ?: mutableMapOf())
        invites[player.lowercase()] = data.apply { put(clan, System.currentTimeMillis() + timeunit.toMillis(time)) }
    }

    fun deleteInvite(player: String, clan: Clan) = invites[player.lowercase()]?.remove(clan)

    fun hasPendingInvite(player: String, clan: Clan) = invites[player.lowercase()]?.get(clan)?.run {
        this >= System.currentTimeMillis()
    } == true
}