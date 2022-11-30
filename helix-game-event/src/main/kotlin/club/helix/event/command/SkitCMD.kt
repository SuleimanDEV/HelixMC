package club.helix.event.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.event.EventPlugin
import club.helix.event.player.GamePlayer
import club.helix.event.player.GamePlayerType

class SkitCMD(private val plugin: EventPlugin): BukkitCommandExecutor() {

    @CommandOptions(
        name = "skit",
        target = CommandTarget.PLAYER,
        permission = true,
        description = "Aplicar seu inventário para os jogadores."
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val senderInventory = sender.player.inventory

        plugin.game.getPlayers(GamePlayerType.PLAYING).map(GamePlayer::player).forEach {
            it.inventory.apply {
                armorContents = senderInventory.armorContents
                contents = senderInventory.contents
            }
            it.updateInventory()
        }

        sender.sendMessage("§aSeu inventário foi aplicado para todos os jogadores.")
    }
}