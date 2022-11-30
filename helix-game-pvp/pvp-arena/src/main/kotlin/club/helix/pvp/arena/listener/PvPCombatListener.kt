package club.helix.pvp.arena.listener

import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.kotlin.player.allowedPvP
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class PvPCombatListener(private val plugin: PvPArena): Listener {
    companion object {
        private val blockedCommands = arrayOf("spawn")
    }

    @EventHandler (ignoreCancelled = true)
    fun onExecuteCommand(event: PlayerCommandPreprocessEvent) = event.takeIf {
        plugin.combatLog.contains(event.player.name) && it.message.replace("/", "").let {
            blockedCommands.any { blockedCmd -> blockedCmd.lowercase() == it.lowercase() }
        }
    }?.run {
        event.isCancelled = true
        event.player.sendMessage("§cVocê não pode executar comandos em combate.")
    }

    @EventHandler fun onDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        (it.entity as? Player)?.allowedPvP == true && (it.damager as? Player)?.allowedPvP == true
    }?.run {
        plugin.combatLog.apply { set(entity.name); set(damager.name) }
    }
}