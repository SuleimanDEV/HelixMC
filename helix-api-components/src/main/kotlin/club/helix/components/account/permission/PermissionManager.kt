package club.helix.components.account.permission

import club.helix.components.account.HelixRank

class PermissionManager(
    val permissionLoader: PermissionLoader
) {
    val permissions = mutableMapOf<HelixRank, ArrayList<String>>()

    fun getPermissions(rank: HelixRank) =
        permissions.getOrDefault(rank, mutableListOf())
}