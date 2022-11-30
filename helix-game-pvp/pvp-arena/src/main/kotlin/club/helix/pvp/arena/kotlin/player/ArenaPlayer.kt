package club.helix.pvp.arena.kotlin.player

import club.helix.pvp.arena.PvPArena
import org.bukkit.entity.Player

val Player.arenaPlayer get() = PvPArena.instance.arenaPlayers.firstOrNull {
    it.name == name } ?: throw NullPointerException("arena player is invalid")