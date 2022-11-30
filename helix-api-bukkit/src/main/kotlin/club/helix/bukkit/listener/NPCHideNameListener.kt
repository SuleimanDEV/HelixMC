package club.helix.bukkit.listener

import club.helix.bukkit.npc.HelixNPC
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class NPCHideNameListener: Listener {

    @EventHandler fun onJoin(event: PlayerJoinEvent) =
        HelixNPC.npcs.forEach { it.hideName(event.player) }
}