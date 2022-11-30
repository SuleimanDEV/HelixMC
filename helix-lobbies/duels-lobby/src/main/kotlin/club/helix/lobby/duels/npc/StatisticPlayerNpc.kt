package club.helix.lobby.duels.npc

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.inventory.statistic.DuelsInventory
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.npc.HelixSimpleNPC
import org.bukkit.Location
import org.bukkit.entity.Player

class StatisticPlayerNpc: HelixSimpleNPC(
    arrayOf(
        "§b§lSUAS ESTATíSTICAS",
        "§7(Clique para ver)"
    ),
    "skind17f915b",
    "ewogICJ0aW1lc3RhbXAiIDogMTYyMzM0NTU4NTg1MiwKICAicHJvZmlsZUlkIiA6ICIxYWZhZjc2NWI1ZGY0NjA3YmY3ZjY1ZGYzYWIwODhhOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJMb3lfQmxvb2RBbmdlbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iZWU3MThjYmY3MWIzY2JkNTA4ZWY1NDJkMzUzZDkwOTlmMWE3ZWYxMzg5NGNjZDIxZjY2NzljZmYzMDllZjAiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
    "BMnej9gqGZwjPm6Vvvfd9bqtlmVI1DjQ84hQCQnMJHu5yweR5WqEhgBD6knoq5dKC+dLBnotWZaG4wB96E1JA6dYtqgrld0eCFDJNGMCSZNt//1NX/uxHGfhl4TK3bxTbbAqBcqJelXCm4akt0Vaah23OhZ2Cot9IPN085qjXHRpOTX3diFP/Yb/JcSRBSpHzvHYPZnrO+UJRyHj6jJDeekFWldS3ZiU6dupCBczcQuikFZm9C/jrT5SweetKxjLkTdcuNqjvNPXAfPbfRkK5paVvN0k3lADdwzaxD/M3BUARz5NcC9XAAxl7Ss8hlX4nVEhkvtyMT1zK3pG87wuBDaT9bD45NQpKqMHSeuCNx1WFG2JApARDh7fug0HfGOfzWVNW2I6LLE21fjJ38H9ldLMvW9OVoUoaMha1SRVssBigDJSWl90KaZRYIZY3yEBltnCx/8Xpo9acj6gUENG0gfjHkOfBuENo3y8zgFtHZrmozwaSUCtqgu9Xu3OiEJIGrxYvKtAW5t/STHZlL1qoKvhpvfI6rxZMCFpQUfCAdoEd6BOKxqoUuH2UcO1RBnpngpidz1SiIgaJPcPxIgbAQg831jaBJZWsLMJJgfQeMLu6QYiXa03W6rEvEZWZ1AX9kZGOwwcdM1y9e2aT4a11ANvsbdcPImZL0MnRoHuf4E=",
    Location(HelixBukkit.instance.world, 6.469925584607228, 63.0, 13.519975792889392, 162.90222f, -1.4999185f)
) {
    fun load() = spawn {
        val player = it.clicker as Player
        DuelsInventory(player.account).open(player, false)
    }
}