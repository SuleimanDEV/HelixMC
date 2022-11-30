package club.helix.pvp.lava.kotlin.player

import club.helix.pvp.lava.LavaPlayer
import club.helix.pvp.lava.PvPLava
import org.bukkit.entity.Player

val Player.lavaPlayer get() = PvPLava.instance.lavaPlayers.firstOrNull {
    it.name == name } ?: throw NullPointerException("invalid lava player")

fun Player.registerLavaPlayer() = PvPLava.instance.lavaPlayers.takeIf {
    !it.any { lavaPlayer  -> lavaPlayer.name == name } }?.add(LavaPlayer(name))

fun Player.unregisterLavaPlayer() = PvPLava.instance.lavaPlayers
    .removeIf { it.name == name }