package club.helix.pvp.arena.kotlin.player

import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.kit.ArenaKit
import org.bukkit.entity.Player

fun Player.hasKitPermission(kit: ArenaKit) = kit == ArenaKit.NONE || kit.price == 0 || hasPermission("pvp.arena.kit.${kit.toString().lowercase()}") ||
        hasPermission("pvp.arena.kit.*") || PvPArena.instance.arenaUserKits.hasKit(name, kit)