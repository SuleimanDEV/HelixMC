package club.helix.bungeecord.kotlin.clan

import club.helix.components.clan.Clan
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.BaseComponent

fun Clan.broadcast(component: Array<BaseComponent>) = ProxyServer.getInstance().players.filter {
    containsMember(it.name)
}.forEach { it.sendMessage(*component) }