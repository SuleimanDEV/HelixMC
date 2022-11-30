package club.helix.pvp.fps.kotlin.player

import club.helix.pvp.fps.PvPFps
import org.bukkit.entity.Player

val Player.allowedPvP get() = PvPFps.instance.arenaPlayers.contains(name)

fun Player.addPvP() = PvPFps.instance.arenaPlayers.takeIf {
    !it.contains(name) }?.add(name)

fun Player.removePvP() = PvPFps.instance.arenaPlayers.remove(name)