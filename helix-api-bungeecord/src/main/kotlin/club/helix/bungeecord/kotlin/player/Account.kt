package club.helix.bungeecord.kotlin.player

import club.helix.bungeecord.HelixBungee
import club.helix.components.account.HelixUser
import net.md_5.bungee.api.connection.ProxiedPlayer

val ProxiedPlayer.account get(): HelixUser = HelixBungee.instance.components.userManager.getUser(name)
    ?: throw NullPointerException("account not found")