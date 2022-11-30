package club.helix.pvp.api.listener

import club.helix.bukkit.event.PlayerDeathEvent
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.nms.NameTagNMS
import club.helix.components.account.game.PvP
import club.helix.pvp.api.GameAPI
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class UserKillPlayerListener(private val gameAPI: GameAPI): Listener {
    companion object {
        const val RECEIVE_XP = 20
        const val WITHDRAWN_XP = 5
    }

    @EventHandler (priority = EventPriority.HIGH)
    fun onDeath(event: PlayerDeathEvent) = event.takeIf {
        it.hasKiller()
    }?.apply {
        killer.account.let { user ->
            val response = user.pvp.addExp(RECEIVE_XP)
            val rank = user.pvp.rank

            if (response == PvP.LevelResponse.LEVEL_UP) {
                killer.playSound(killer.location, Sound.LEVEL_UP, 15.0f, 15.0f)
                Bukkit.broadcastMessage("${user.tag.color}${killer.name} §bupou para o rank ${rank.color}${rank.displayName} ${user.pvp.romanNumeral(user.pvp.subrank)}§b!")
                NameTagNMS.setSuffix(killer, " ${user.pvp.displayRank()}")
            }

            if (response == PvP.LevelResponse.SUCCESS) {
                killer.sendMessage("§e+$RECEIVE_XP xp")
            }
            gameAPI.apiBukkit.components.userManager.saveAll(user, false)
        }

        player.account.let { user ->
            val response = user.pvp.removeExp(WITHDRAWN_XP)

            if (response == PvP.LevelResponse.LEVEL_DOWN) {
                player.sendMessage("§cVocê foi rebaixado para a patente ${user.pvp.rank.displayName} ${user.pvp.romanNumeral(user.pvp.subrank)}.")
            }else if (response == PvP.LevelResponse.SUCCESS) {
                player.sendMessage("§c-$WITHDRAWN_XP xp")
            }
            gameAPI.apiBukkit.components.userManager.saveAll(user, false)
        }
    }
}