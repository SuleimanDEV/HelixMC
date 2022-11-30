package club.helix.lobby.provider.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.account
import club.helix.components.HelixComponents
import club.helix.components.util.HelixTimeData
import club.helix.lobby.provider.util.PlayerVisible
import club.helix.lobby.provider.util.SpawnItems
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import java.util.concurrent.TimeUnit

class ChangePlayerVisibleListener(private val api: HelixComponents): Listener {

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.apply {
        if (!hasItem() || !ItemBuilder.has(item, "lobby-item", "visible-players"))
            return@apply
        isCancelled = true

        if (HelixTimeData.getOrCreate(player.name, "change-player-visible", 7, TimeUnit.SECONDS)) {
            return@apply player.sendMessage("§cAguarde ${HelixTimeData.getTimeFormatted(player.name, "change-players-visible")} para alterar a visibilidade novamente.")
        }

        val user = player.account.apply {
            this.preferences.lobby.let { it.playersVisible = !it.playersVisible }
            api.userManager.redisController.save(this)
        }

        val visible = user.preferences.lobby.playersVisible
        PlayerVisible.handle(player)
        SpawnItems.set(player)

        player.sendMessage(if (visible) "§aVocê ativou a visibilidade dos jogadores." else "§cVocê desativou a visibilidade dos jogadores.")
    }
}