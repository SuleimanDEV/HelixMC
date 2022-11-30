package club.helix.components.clan

import club.helix.components.account.HelixRank
import club.helix.components.clan.exception.ClanMemberAlreadyJoinedException
import club.helix.components.clan.exception.ClanMemberNotFoundException

class Clan(
    val name: String,
    val tag: String,
    val createdAt: Long = System.currentTimeMillis(),
    val members: MutableList<ClanMember> = mutableListOf()
) {
    var maxMembers: Int = 8
    var maxManagers: Int = 3
    var tagColorCode: Int = 7
    var exp: Int = 0
    var kills: Int = 0
    val leader get() = members.first { it.role == ClanRole.LEADER }
    val managers get() = members.filter { it.role == ClanRole.MANAGER }

    fun removeMember(member: ClanMember) {
        if (!containsMember(member.name)) throw ClanMemberNotFoundException()
        members.remove(member)
    }

    fun addMember(name: String, role: ClanRole = ClanRole.MEMBER) {
        if (containsMember(name)) throw ClanMemberAlreadyJoinedException()
        ClanMember(name, role).apply { members.add(this) }
    }

    fun containsMember(name: String) = members.any {
        it.name.lowercase() == name.lowercase() }

    fun getMember(name: String) = members.firstOrNull {
        it.name.lowercase() == name.lowercase() }

    fun recalculate(leaderRank: HelixRank): Boolean {
        val maxMembers = if (HelixRank.vip(leaderRank)) 8 else 5
        val maxManagers = if (HelixRank.vip(leaderRank)) 3 else 2
        val tagColor = if (exp >= 10000) 6 else 7

        if (this.maxMembers == maxMembers && this.maxManagers == maxManagers &&
                this.tagColorCode == tagColor) return false

        this.maxMembers = maxMembers
        this.maxManagers = maxManagers
        this.tagColorCode = tagColor
        return true
    }
}