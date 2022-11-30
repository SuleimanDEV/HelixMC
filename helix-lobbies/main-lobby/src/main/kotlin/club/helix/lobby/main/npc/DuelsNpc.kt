package club.helix.lobby.main.npc

import club.helix.bukkit.HelixBukkit
import club.helix.components.server.HelixServer
import club.helix.lobby.provider.npc.DisplayNpcServer
import org.bukkit.Location

class DuelsNpc: DisplayNpcServer(
    arrayOf("§5§l> §d§l1V1 + GLADIATOR §5§l<", "§bDuels"),
    HelixServer.DUELS, null,
    HelixServer.Category.LOBBY,
    "skin5ee6770a",
    "ewogICJ0aW1lc3RhbXAiIDogMTYzMjYzODE2NjA4OCwKICAicHJvZmlsZUlkIiA6ICI2NDU4Mjc0MjEyNDg0MDY0YTRkMDBlNDdjZWM4ZjcyZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaDNtMXMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgwNTUzODU1MjAzZGQ0MTY0MjM5MGYxMWE4MGZjMjJkYjA5MDdmNGRiMTU2OTRlOWUxZTkwYTA5MmU4OGU2MiIKICAgIH0KICB9Cn0=",
    "LkwPFyePt5+0A0JWtYzRQKIq3PSn181EW8eU8b9QQu9bDM4OmfTanVCoBVNsqkIMploHupwnCHirMeX8fHtAkNHZOS2Ub3FncCjWYiMsXrQsHs53mp440G7nfjCQNKQDGFYmHuyZW2OznOWfKSqnqSFZpCjyjfDSyb1qijCr6P9X95nM+uwiTCXiOU5IfFOffPBhCqH1+HI7nNMKDpGryaPXQbC3iZN/pqB7HqOXFbbDekDjeCdAMxa4U4a4zKC043FxqUqLEmu/xXE7adf94ciW/Fco98AtYKtDKs2V06oK+zcn136KiiVMAC8UeQruFzV1XZsFDHPzmYlEav3vTPzVVCByQxHD9q5Bku+aNXrx8bw+rC+8VhK25ZM9iUkDzeP3UJ8VZ6ncLpvN+Hl0F0tXmtOVoeuiyjV45nIsuHu437ECx6Xifvdv8wCzibpGdEYekP23nWE0Enczq8f7N/8ZecwYkTzE0owz7vF2hvOSwOLhH+2E2oZv00iW2GbEshl7Q628uCaSeL4wb8E69VCsSfzv7TdpFcf5J8wIbSu5OJtf8p2W5/FXcZbGxf5Xt/DUwMLRCMXGq9+JKl/Kp43wahwXv1epljnI/h4tCDO2OkAPNLt+c5KEGNkaJyLq8a6iKqxyh5onhELLjgrGyBgzjeH+Oc7/llPV6Dm7PcY=",
    Location(HelixBukkit.instance.world, -2.486805075765633, 75.0, 17.439599302341794, 179.70001f, -1.7999989f),
)