package club.helix.pvp.arena.vip

import club.helix.components.account.HelixRank
import club.helix.pvp.arena.kit.ArenaKit

class RanksKitsPermission {
    companion object {
        fun load() {
            HelixRank.values().filter { HelixRank.staff(it) }.forEach {
                it.addPermission("pvp.arena.kit.*") }

            arrayOf(
                HelixRank.YOUTUBER,
                HelixRank.BETA,
                HelixRank.ULTRA,
                HelixRank.DESIGNER,
                HelixRank.BUILDER
            ).forEach { it.addPermission("pvp.arena.kit.*") }

            arrayOf(
                ArenaKit.BOXER, ArenaKit.FIREMAN,
                ArenaKit.FLASH, ArenaKit.VIPER,
                ArenaKit.NINJA, ArenaKit.FISHERMAN,
                ArenaKit.GRANDPA, ArenaKit.CAMEL
            ).forEach { HelixRank.VIP.addPermission("pvp.arena.kit.${it.toString().lowercase()}") }

            arrayOf(
                ArenaKit.FIREMAN, ArenaKit.FLASH,
                ArenaKit.NINJA, ArenaKit.GRANDPA, ArenaKit.BOXER
            ).forEach { HelixRank.BLADE.addPermission("pvp.arena.kit.${it.toString().lowercase()}") }
        }
    }
}