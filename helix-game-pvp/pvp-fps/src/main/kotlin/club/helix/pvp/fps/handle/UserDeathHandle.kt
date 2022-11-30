package club.helix.pvp.fps.handle

import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.util.LastKillsUtils
import club.helix.components.account.HelixRank
import club.helix.pvp.fps.PvPFps
import club.helix.pvp.fps.kotlin.player.removePvP
import club.helix.pvp.fps.vip.CoinsMultiplier
import club.helix.pvp.fps.vip.KillMessages
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class UserDeathHandle(
    private val victim: Player,
    private val killer: Player,
    private val plugin: PvPFps
) {

    private val repeatedKill = LastKillsUtils.isRepeatedKill(killer, victim)
    private val victimAccount = victim.account
    private val killerAccount = killer.account

    fun execute() {
        killerHandle()
        victimHandle()
    }

    private fun killerHandle() {
        killer.inventory.armorContents.forEach { it.durability = 0 }
        killer.sendMessage("§aVocê matou ${victim.name}§a.${if (repeatedKill) " §7(Não conta)" else ""}")

        val user = killerAccount.apply {
            if (repeatedKill) return@apply

            val multiplier = CoinsMultiplier(mainRankLife.rank)
            val addedCoins = multiplier.calcule().apply {
                pvp.coins += this
                pvp.fps.kills++
                pvp.fps.killstreak++

                if (pvp.fps.maxKillStreak < pvp.fps.killstreak) {
                    pvp.fps.maxKillStreak = pvp.fps.killstreak
                }
            }
            plugin.apiBukkit.components.userManager.saveAll(this)
            killer.sendMessage("§6+$addedCoins coins${if (HelixRank.vip(mainRankLife.rank)) " (Bõnus Vip: ${multiplier.boost}x)" else ""}")
        }

        user.pvp.fps.killstreak.takeIf {
            it != 0 && (it.toString().contains("5") || it.toString().contains("0"))
        }?.let { streak ->
            val streakColor = when (streak) {
                3 -> "§d"
                5 -> "§e"
                10 -> "§6"
                15 -> "§c"
                else -> "§9"
            }

            Bukkit.broadcastMessage("${user.tag.color}${killer.name} §aatingiu um killstreak de $streakColor$streak§a!")
        }
        if (HelixRank.vip(user.tag)) {
            Bukkit.broadcastMessage(
                KillMessages.values().random().message
                .replace("{killer}", "${user.tag.color}${killer.name}")
                .replace("{victim}", "${victimAccount.tag.color}${victim.name}"))
        }
    }

    private fun victimHandle() = victim.apply {
        removePvP()
        activePotionEffects.forEach { removePotionEffect(it.type) }

        plugin.server.scheduler.runTaskLater(plugin, {
            plugin.serverSpawnHandle.send(player)
        }, 5)

        val random = Random()
        sendMessage("§cVocê morreu para ${killer.name}.")

        victimAccount.apply {
            val withdrawnCoins = random.nextInt(17 + 1 - 3) + 3
            pvp.fps.apply { deaths++; killstreak = 0 }

            pvp.coins.takeIf { it > 0 }?.let {
                if ((it - withdrawnCoins) >= 0) {
                    pvp.coins -= withdrawnCoins
                }else {
                    pvp.coins = 0
                }
            }
            plugin.apiBukkit.components.userManager.saveAll(this)
            sendMessage("§c-$withdrawnCoins coins")
        }
    }
}