package club.helix.pvp.arena.kotlin.player

import club.helix.pvp.arena.PvPArena
import org.bukkit.entity.Player

fun Player.unregisterArenaPlayer() = PvPArena.instance.arenaPlayers.removeIf {
    it.name == name }