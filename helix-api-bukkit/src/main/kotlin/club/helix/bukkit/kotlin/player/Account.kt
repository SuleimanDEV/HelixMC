package club.helix.bukkit.kotlin.player

import club.helix.bukkit.HelixBukkit
import club.helix.components.account.HelixUser
import org.bukkit.entity.Player

val Player.account get(): HelixUser = HelixBukkit.instance.components.userManager.getUser(name)
    ?: throw NullPointerException("account not found!")