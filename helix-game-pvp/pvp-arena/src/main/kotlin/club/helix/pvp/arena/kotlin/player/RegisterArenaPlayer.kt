package club.helix.pvp.arena.kotlin.player

import club.helix.pvp.arena.ArenaPlayer
import club.helix.pvp.arena.PvPArena
import org.bukkit.entity.Player

fun Player.registerArenaPlayer() = PvPArena.instance.arenaPlayers.takeIf {
    !it.any { player -> player.name == name } }?.add(ArenaPlayer(name))