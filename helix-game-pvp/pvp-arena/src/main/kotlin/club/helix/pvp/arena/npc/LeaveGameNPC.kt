package club.helix.pvp.arena.npc

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.npc.HelixSimpleNPC
import club.helix.components.server.HelixServer
import org.bukkit.Location

class LeaveGameNPC: HelixSimpleNPC(
    arrayOf("§b§lVOLTAR AO LOBBY", "§7(Clique aqui)"),
    "skin48e0cddf",
    "ewogICJ0aW1lc3RhbXAiIDogMTY0MDM1MDA0OTQzNywKICAicHJvZmlsZUlkIiA6ICJiMGQ3MzJmZTAwZjc0MDdlOWU3Zjc0NjMwMWNkOThjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPUHBscyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83YzYyN2Y4MTU5NGYzMDhhMTI3YWY1NjBhMTVhY2QxMTUyNzZhNjYwOWFiOWVlNGU0YWFiYTE5NDg4OGM4ODY3IgogICAgfQogIH0KfQ==",
    "rqxxCmpaUiEDQCOXJsMnjX+474EplD74ImrQGABPhmAk+S1gEhE9MKqHmkf3JqEjBAjG5KNFgfhszeRE2E1QgOSZ1C0+vfTgBHo0XJ+loS6gO4xU1MQ4v2ZvZM34vRDBYyoM25hHaxZl7EIv91kjSf3dGl2OgBl+/33fbc8U9c0bbCZBgUtwdGsQj8j+7LQC5LFWI9vO6B/YPwEU5AXr1/2Ank8xM6/AojOUWYyEZfWJtoMVbH4KDdom9iHr0MkSGkCCY9vIvqNtCERncEDy2HHpeZyKcWXGrT3vIJK7wznpW+JlM8e1PaCm7yzd+CWhwmOppsvunErSeR2SWthapOyCD8zMBkwcVnCSSyt0ob5v2aodQFZV6xO0BqSwda6fytu9OZGUh5GECFSaCwGdRlQcJVYkjA8FZnhST5O8QILgEsmSW+g5vpd6uhNtjXx/KWWfbnMT0cmtDzRpxQIfpQpowxmWFxuF7PUPvtz3hq0SMOL6bwPV54q+WG/gME28ZqVGdQqCtlya27nOvpjwaEF6kOqmEvNQ6zIwO6o3gDMAR5ojjSiAMtKWPIQ0Jl+WpfDwYjGkoYuY4TcOSBVocciz2/i74GJ1r0sZh1tCUfDyBGt3mD8HRovuc7XUeHoj72ijhN/83aJt/oINX+o/gvDG0rijXt422ifV9CMp5n4=",
    Location(HelixBukkit.instance.world, 28.28745638291666, 209.0625, -19.853639315448, 292.2968f, -1.6501504f)
) {
    fun load() = spawn {
        HelixServer.PVP.findAvailable(HelixServer.Category.LOBBY)?.apply {
            it.clicker.connect(this)
        } ?: return@spawn it.clicker.sendMessage("§cNão há um lobby disponível.")
    }
}