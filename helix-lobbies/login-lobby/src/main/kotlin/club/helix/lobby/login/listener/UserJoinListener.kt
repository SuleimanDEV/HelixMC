package club.helix.lobby.login.listener

import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.kotlin.player.sendPlayerTitle
import club.helix.bukkit.kotlin.player.teleport
import club.helix.bukkit.nms.NameTagNMS
import club.helix.components.account.HelixRank
import club.helix.lobby.login.LoginLobby
import club.helix.lobby.login.inventory.CaptchaInventory
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.concurrent.TimeUnit

class UserJoinListener(private val plugin: LoginLobby): Listener {

    @EventHandler fun onJoin(event: PlayerJoinEvent) = event.player.apply {
        gameMode = GameMode.ADVENTURE
        maxHealth = 20.0
        health = maxHealth
        foodLevel = 20
        level = 0
        exp = 0f
        allowFlight = false
        isFlying = allowFlight
        activePotionEffects.forEach { removePotionEffect(it.type) }
        plugin.loggingPlayers[name] = System.currentTimeMillis()

        val user = account
        val registered = user.login.isRegistered()

        if (!registered || !HelixRank.vip(user.mainRankLife.rank) && TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - user.login.lastLogin) >= 4) {
            CaptchaInventory.open(this)
        }

        user.tag = HelixRank.DEFAULT
        NameTagNMS.setNametag(this, "§7", 0, NameTagNMS.Reason.CUSTOM)
        sendPlayerTitle("§b§lHELIX", if (registered) "/login <senha>" else "/register <senha> <senha>", 40)

        Bukkit.getOnlinePlayers().forEach {
            it.hidePlayer(this)
            hidePlayer(it)
        }
        teleport(297.495, 81.0, 300.528, -179.7f, -1.6f)
        (if (registered) plugin.loginScoreboard else plugin.registerScoreboard).build(this)
    }
}