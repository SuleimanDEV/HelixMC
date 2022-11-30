package club.helix.bukkit.listener

import club.helix.bukkit.npc.HelixNPC
import net.citizensnpcs.api.event.NPCLeftClickEvent
import net.citizensnpcs.api.event.NPCRightClickEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class NpcInteractListener: Listener {

    @EventHandler fun onRightClick(event: NPCRightClickEvent) = event.npc.data().get(HelixNPC.dataKey, String())?.let { name ->
        HelixNPC.npcs.firstOrNull { it.name == name }?.consumer?.accept(event)
    }

    @EventHandler fun onLeftClick(event: NPCLeftClickEvent) = event.npc.data().get(HelixNPC.dataKey, String())?.let { name ->
        HelixNPC.npcs.firstOrNull { it.name == name }?.consumer?.accept(event)
    }
}