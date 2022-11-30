package club.helix.pvp.arena.kit

import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.kotlin.player.allowedPvP
import club.helix.pvp.arena.kotlin.player.hasSelectedKit
import org.bukkit.entity.Player
import org.bukkit.event.Listener

abstract class KitHandler: Listener {

    val plugin = PvPArena.instance

    open protected fun handleApply() {
        handleApply()
    }

    open fun apply(player: Player) {}
    fun valid(player: Player) = player.allowedPvP && player.hasSelectedKit(this)
}