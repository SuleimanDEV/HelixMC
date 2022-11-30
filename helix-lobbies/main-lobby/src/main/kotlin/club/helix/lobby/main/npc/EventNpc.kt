package club.helix.lobby.main.npc

import club.helix.bukkit.HelixBukkit
import club.helix.components.server.HelixServer
import club.helix.lobby.provider.npc.DisplayNpcServer
import org.bukkit.Location

class EventNpc: DisplayNpcServer(
    arrayOf("§bEventos"),
    HelixServer.SPECIAL, null,
    HelixServer.Category.EVENT,
    "SmarttBR",
    "",
    "",
    Location(HelixBukkit.instance.world, 8.513605166896127, 75.0, 15.477553747171696, 179.25012f, -2.5500393f),
)