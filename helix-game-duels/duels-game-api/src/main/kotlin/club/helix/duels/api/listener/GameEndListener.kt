package club.helix.duels.api.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.kotlin.player.sendPlayerTitle
import club.helix.duels.api.DuelsAPI
import club.helix.duels.api.event.GameEndEvent
import club.helix.duels.api.game.DuelsPlayer
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameEndListener(private val duelsAPI: DuelsAPI<*>): Listener {

    @EventHandler fun onGameEnd(event: GameEndEvent<*>) = event.apply {
        val players = game.playingPlayers.map(DuelsPlayer::player).filter(Player::isOnline)

        loser.player.takeIf(Player::isOnline)?.run {
            player.sendMessage("§cVocê perdeu o jogo!")
            player.sendPlayerTitle("§c§lVOCê PERDEU!", "§7Mais sorte na próxima vez.", 10)
        }

        winner.player.apply {
            sendMessage("§aVocê ganhou o jogo!")
            sendPlayerTitle("§6§lVITóRIA", "§7Você foi o último sobrevivente.", 10)
            playSound(winner.player.location, Sound.ORB_PICKUP, 10.0f, 10.0f)

            val addXp = 20
            account.apply {
                duels.addExp(addXp)
                duelsAPI.components.userManager.saveAll(this)
            }
            sendMessage("§b+$addXp exp")
        }

        game.spectatorPlayers.forEach {
            it.sendMessage("§cFim de jogo! Vencedor: ${winner.player.name}")
            it.sendPlayerTitle("§c§lFIM DE JOGO!",
                "§7Vencedor: ${winner.player.account.mainRankLife.rank.color}${winner.player.name}", 10)
        }

        players.forEach {
            it.closeInventory()
            it.inventory.clear()
            it.inventory.armorContents = null
            it.health = it.maxHealth
            it.fireTicks = -10
            it.allowFlight = true
            it.isFlying = it.allowFlight
            it.inventory.heldItemSlot = 2

            it.inventory.setItem(0, ItemBuilder()
                .type(Material.PAPER)
                .displayName("§aJogar novamente")
                .identify("cancel-drop")
                .identify("spectator-item", "play-again")
                .toItem
            )

            it.inventory.setItem(8, ItemBuilder()
                .type(Material.BED)
                .displayName("§aVoltar ao lobby")
                .identify("cancel-drop")
                .identify("back-to-lobby-item")
                .toItem
            )
        }
    }
}