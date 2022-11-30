package club.helix.bukkit.command

import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.completer.CommandCompleter
import club.helix.components.command.executor.CommandOptions
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class GodCMD: BukkitCommandExecutor(), Listener {

    companion object {
        val godPlayers = mutableListOf<String>()
    }

    override fun onTabComplete(sender: BukkitCommandSender) = mutableListOf(
        CommandCompleter(0, Bukkit.getOnlinePlayers().map(Player::getName).toMutableList())
    )

    @CommandOptions(
        name = "god",
        permission = true,
        description = "Gerenciar modo invencível"
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        if (args.isEmpty() && sender.isConsole) {
            return sender.sendMessage("§cUtilize /god <jogador> para gerenciar o modo invencível dos jogadores.")
        }

        val target = Bukkit.getPlayer(if (args.isNotEmpty()) args.first() else sender.name)

        if (sender.name != target.name && !sender.hasPermission("helix.cmd.god.other")) {
            return sender.sendMessage("§cVocê não tem permissão para gerenciar o modo invencível dos jogadores.")
        }

        godPlayers.takeIf { it.contains(target.name.lowercase()) }?.remove(target.name.lowercase())
            ?: godPlayers.add(target.name.lowercase())

        val enable = godPlayers.contains(target.name.lowercase())

        if (enable) {
            target.apply { health = maxHealth; foodLevel = 20 }
            target.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 5))
        }

        if (sender.name == target.name) {
            sender.sendMessage(if (enable) "§aVocê ativou o modo invencível." else
                "§eVocê desativou o modo invencível.")
        }else {
            sender.sendMessage(if (enable) "§aVocê ativou o modo invencível de §f${target.name}§a." else
                "§eVocê desativou o modo invencível de §${target.name}§e.")
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) = godPlayers.removeIf { it.lowercase() == event.player.name.lowercase() }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) = godPlayers.takeIf {
        it.contains(event.entity.name.lowercase())
    }?.run { event.isCancelled = true }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) = godPlayers.takeIf {
        it.contains(event.entity.name.lowercase())
    }?.run { event.isCancelled = true }

    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) = godPlayers.takeIf {
        it.contains(event.entity.name.lowercase())
    }?.run { event.isCancelled = true }
}