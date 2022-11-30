package club.helix.pvp.fps.npc

import club.helix.components.server.HelixServer
import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.npc.HelixSimpleNPC
import org.bukkit.Location

class LeaveGameNPC: HelixSimpleNPC(
    arrayOf("§b§lVOLTAR AO LOBBY", "§7(Clique aqui)"),
    "skin4b6b6baa",
    "ewogICJ0aW1lc3RhbXAiIDogMTYzMDg5NDIxNDA0NCwKICAicHJvZmlsZUlkIiA6ICJjNTBhZmE4YWJlYjk0ZTQ1OTRiZjFiNDI1YTk4MGYwMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUd29FQmFlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQzY2IxMjE2YmY4YzY2ZDk5ZWM4M2Y0ZGUyODQzYzUzMTIxODJlMjgyM2JjNDEyN2NkMTVhOTdiODZlNTczMmIiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
    "Gnex8yMUDby09K7Jkz3VA4iT0OZh5mmQA31PuxYdJnUsjQqdKlWvhNGXHhwnEsdzQupui/O4rS/g/1bK6pZAQuiuf2GAmUogdC8n8b3xoNGuj7SPGk8CRCxptcbREnGcUPhXHyng1krPbmOrHczuePOB/hK7ByAlZO/OFZlwUiJAhd4ylN+qFkNqRFeYojTSWWGbX74OYCMNe7rb3nvxwvZAA8E/uFcItlD4ZMzC6mc1tBtBjbu8dAzAyAJEwbBylR37EnTNGn6G0xhahdjPzNvty/W00ZMmswB7pQq0Ppfx1aVq0wziSP85YYVei0lx64TipR3wY837mc+DQDFQDRJNeWXkxbhdMAxvOOizQ7vK4/5DZMElakjXMdsjBtxLyaEiJ/6XypFdmATcmslClnwPkAk4pVoMLEmKwxFBUenNnE4kOcoVAHI4cXLY3ulcH3R0vdEPBPjcpRMooae94wQxDQ8DtrMIGikU8tDIlMceEmpt/svlHcZ/GOzHK47+8x0Qe4JMX/utMuPRT33TbXwDNG3LC4WJKhpWVgZFnovpll/eGZv1zCyGPeNvrlKHWffm9Uy8rHK1xKb7tIRrHxDEWV0JLMADLIKPvL1xEaVgoC/14xINVGgIvy+fmw6oNSDj3kNA4rhxDk4+bZ8K4veeGgxSWii0eu9dDj/K7xw=",
    Location(HelixBukkit.instance.world, 2.4798452124763743, 65.0, -1.482275218208289, -315.15002f, 3.0000286f)
) {
    fun load() = spawn {
        HelixServer.PVP.findAvailable(HelixServer.Category.LOBBY)?.apply {
            it.clicker.connect(this)
        } ?: return@spawn it.clicker.sendMessage("§cNão há um lobby disponível.")
    }
}