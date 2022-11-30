package club.helix.lobby.pvp.listener

import club.helix.lobby.pvp.PvPLobby
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent

class UserPortalListener(private val plugin: PvPLobby): Listener {

    @EventHandler (ignoreCancelled = true)
    fun onPortal(event: PlayerPortalEvent) = event.takeIf {
        it.cause == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL
    }?.apply {
        player.teleport(plugin.spawnLocation)

        player.playSound(player.location, Sound.PORTAL_TRAVEL, 10.0f, 10.0f)
        player.sendMessage("§d§lEntão você é do tipo aventureiro?\n§ePena que o portal está liberado somente para §cGuardiões§e.")
    }
}