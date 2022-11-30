package club.helix.components.account.permission

import club.helix.components.account.HelixRank

interface PermissionLoader {
    fun load() = HelixRank.values().forEach { it.permissions.addAll(load(it)) }
    fun load(rank: HelixRank): MutableCollection<String>
    fun unloadFile()
}