package club.helix.pvp.arena.kotlin.player

import club.helix.pvp.arena.kit.ArenaKit
import club.helix.pvp.arena.kit.KitHandler
import org.bukkit.entity.Player

fun Player.hasSelectedKit(kit: ArenaKit) = arenaPlayer.hasSelectedKit(kit)

fun Player.hasSelectedKit(handler: KitHandler) = arenaPlayer.hasSelectedKit(handler)