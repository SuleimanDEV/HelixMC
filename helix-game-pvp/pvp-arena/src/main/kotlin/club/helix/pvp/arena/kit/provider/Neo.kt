package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.kit.KitHandler
import club.helix.pvp.arena.kit.provider.event.PlayerGladiatorEvent
import club.helix.pvp.arena.kotlin.player.allowedPvP
import club.helix.pvp.arena.kotlin.player.hasSelectedKit
import org.bukkit.event.EventHandler

class Neo: KitHandler() {

    @EventHandler (ignoreCancelled = true)
    fun onGladiator(event: PlayerGladiatorEvent) = event.takeIf {
        it.target.let { target -> target.hasSelectedKit(this) && target.allowedPvP }
    }?.apply {
        isCancelled = true
        player.sendMessage("§cVocê não pode puxar jogadores que estão utilizando o kit Neo.")
    }
}