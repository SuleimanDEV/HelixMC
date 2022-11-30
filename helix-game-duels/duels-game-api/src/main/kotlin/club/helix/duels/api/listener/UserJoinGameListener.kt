package club.helix.duels.api.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.nms.NameTagNMS
import club.helix.duels.api.event.UserJoinGameEvent
import club.helix.duels.api.game.DuelsPlayer
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class UserJoinGameListener: Listener {

    @EventHandler (priority = EventPriority.MONITOR)
    fun onJoin(event: UserJoinGameEvent<*>) = event.game.apply {
        val player = event.player
        val user = player.account

        player.apply {
            fireTicks = 0
            gameMode = GameMode.ADVENTURE
            maxHealth = 20.0
            health = maxHealth
            allowFlight = false
            isFlying = allowFlight
            level = 0; exp = 0f
            inventory.clear()
            inventory.armorContents = null
            inventory.heldItemSlot = 0
        }
        player.fireTicks = 0

        val tag = player.account.tag
        NameTagNMS.setNametag(player, player.account.tag.color, tag.ordinal, NameTagNMS.Reason.CUSTOM)

        player.inventory.setItem(8, ItemBuilder()
            .type(Material.BED)
            .displayName("§aVoltar ao Lobby")
            .identify("back-to-lobby-item")
            .toItem)

        playingPlayers.map(DuelsPlayer::player).forEach {
            println("${it.name} showing ${event.player.name}")
            event.player.showPlayer(it)
            it.showPlayer(event.player)
        }
        spectatorPlayers.forEach { it.showPlayer(event.player) }

        broadcast(
            "${user.tag.color}${user.name} §eentrou na sala! §e(§b${playingPlayers.size}§e/§b${maxPlayers}§e)"
        )
    }
}