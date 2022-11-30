package club.helix.pvp.lava.npc

import club.helix.components.server.HelixServer
import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.connect
import club.helix.bukkit.npc.HelixSimpleNPC
import org.bukkit.Location

class LeaveGameNPC: HelixSimpleNPC(
    arrayOf("§b§lVOLTAR AO LOBBY", "§7(Clique aqui)"),
    "skinad1332c0",
    "ewogICJ0aW1lc3RhbXAiIDogMTU4OTM4ODYxODgxOSwKICAicHJvZmlsZUlkIiA6ICI5MThhMDI5NTU5ZGQ0Y2U2YjE2ZjdhNWQ1M2VmYjQxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCZWV2ZWxvcGVyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzlmN2FkZDg4ODU2ZjFkOGQ2ZDZhNTQ1N2Q2MTFiZmM0ODE4NGNiZWQwNWU5MDc3NTlmNjgwNmQzYmRmMWI0YmEiCiAgICB9CiAgfQp9",
    "qbHJTC3V7qlUaBMqeeyD/TIeQBvlHtIDphnSpqntoBinxJGEQjC6HRe8GY/o3FVHZsb2O2PLaD3ItCV/olQTOaytdAgyTmsqtlrM0t7yAtqnrEmOKsk3yFZxthJAkcYwwBebB7z5nuylQ/vMbcOykpbs/X4UEdj5Zc6aQcakeh5CXExUsI9bEfVVUsGnot8T6lQOU1N39qR6cu82c3IRZJ1mFOrC0Ig1PfYzPRitiEf8whYq1X64yuFSWuQ0hSyfPY5UysbxzC2kRpL6yI7cyLnT2TmofjXnZrsBme4Bzkr8JJeikYrpB+521Zz5tcbSU/n4k83WEbJYvrbg4fnUwKwcwERdUnst/Mi2ec7yay7e6iIxNyd5Fs6KK2Ua8hkLQ1phfdkakVCm9c1+1/1ZQaH86Ksp36BeGBe3IX4XUjp8UvWeM8pKXb5q+LQ86RcaXvgEm1zvcO7dkzRyLtaSQxA7VGnT1WVDHbrjawh0eNsnZiGQFCXAekfsHTKZGPqnECUaWrLCFzskdZ3sgWGzl7IRwht2lNJxPB3XxNpYqee+bQQhcd95e9eOqPdNBxdU+I/eANyyeiyZ+rZjKyXVZFtOH2s4Tyso3mS90+btdaKXPQ5VaWKWC6W6Sa4KYqtVtZa8IxdhrIZJ/W2+0lXhYfdYIUvgkVye6sL6X0Hb4Uk=",
    Location(HelixBukkit.instance.world, -3.1746838533803357, 64.0, -7.19658615339268, 324.30008f, -4.05002f)
) {

    fun load() = spawn {
        HelixServer.PVP.findAvailable(HelixServer.Category.LOBBY)?.apply {
            it.clicker.connect(this)
        } ?: return@spawn it.clicker.sendMessage("§cNão há um lobby disponível.")
    }
}
