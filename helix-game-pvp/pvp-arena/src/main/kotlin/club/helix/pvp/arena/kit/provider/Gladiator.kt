package club.helix.pvp.arena.kit.provider

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.event.PlayerDeathEvent
import club.helix.pvp.arena.handle.UserDeathHandle
import club.helix.pvp.arena.kit.KitHandler
import club.helix.pvp.arena.kit.provider.event.PlayerFlashEvent
import club.helix.pvp.arena.kit.provider.event.PlayerGladiatorEvent
import club.helix.pvp.arena.kit.provider.event.PlayerNinjaEvent
import club.helix.pvp.arena.kotlin.player.allowedPvP
import club.helix.pvp.arena.kotlin.player.hasSelectedKit
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent

class Gladiator: KitHandler() {
    companion object {
        val players = mutableMapOf<GladiatorBattle, GladiatorMapGenerator>()
    }

    private val handle = GladiatorHandle(plugin)

    override fun apply(player: Player): Unit = player.inventory.run {
        addItem(ItemBuilder()
            .type(Material.IRON_FENCE)
            .displayName("§aPuxar")
            .identify("cancel-drop")
            .identify("kit", "gladiator")
            .toItem
        )
    }

    @EventHandler (ignoreCancelled = true)
    fun onInteract(event: PlayerInteractEntityEvent) = event.takeIf {
        it.player.hasSelectedKit(this) && !handle.inBattle(it.player.name) &&
                ItemBuilder.has(it.player.itemInHand, "kit", "gladiator") &&
                (it.rightClicked as? Player)?.let { rightClicked ->
                    rightClicked.allowedPvP && !handle.inBattle(rightClicked.name)
                } == true
    }?.apply {
        isCancelled = true
        val target = rightClicked as Player
        val gladiatorEvent = PlayerGladiatorEvent(player, target)
        Bukkit.getPluginManager().callEvent(gladiatorEvent)

        if (gladiatorEvent.isCancelled) return@apply
        handle.createBattle(player, target)
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    fun onDeath(event: PlayerDeathEvent) = event.takeIf { handle.inBattle(it.player.name) }?.apply {
        val gladiator = handle.getGladiator(player.name)!!
        val loser = gladiator.key.getPlayer(player.name)
            ?: throw NullPointerException("invalid loser")
        val winner = gladiator.key.getWinner(loser)
            ?: throw NullPointerException("invalid winner")

        gladiator.value.unload()
        winner.player.teleport(winner.oldLocation)
        dropsLocation = winner.oldLocation
        players.entries.removeIf { it.key.contains(player.name) }
        UserDeathHandle(player, winner.player,  plugin)
    }

    @EventHandler fun onQuit(event: PlayerQuitEvent) = event.takeIf {
        handle.inBattle(it.player.name)
    }?.apply {
        val gladiator = handle.getGladiator(player.name)!!
        val loser = gladiator.key.getPlayer(player.name)
            ?: throw NullPointerException("invalid loser")
        val winner = gladiator.key.getWinner(loser)
            ?: throw NullPointerException("invalid winner")

        gladiator.value.unload()
        winner.player.teleport(winner.oldLocation)
        players.entries.removeIf { it.key.contains(player.name) }
    }

    @EventHandler (ignoreCancelled = true)
    fun onNinja(event: PlayerNinjaEvent) = event.takeIf {
        handle.getGladiator(it.player.name)?.key?.let { battle ->
            !battle.contains(it.target.name)
        } == true
    }?.apply { isCancelled = true }

    @EventHandler (ignoreCancelled = true)
    fun onFlash(event: PlayerFlashEvent) = event.takeIf {
        handle.inBattle(it.player.name) }?.apply { isCancelled = true }

    @EventHandler (ignoreCancelled = true)
    fun onTeleport(event: PlayerTeleportEvent) = event.takeIf {
        it.cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL &&
                handle.inBattle(it.player.name)
    }?.apply { isCancelled = true }

    @EventHandler (ignoreCancelled = true)
    fun onCommandPreProcess(event: PlayerCommandPreprocessEvent) = event.takeIf {
        handle.inBattle(it.player.name) && event.message.split(" ")[0]
            .replace("/", "").lowercase() == "spawn"
    }?.apply { isCancelled = true }
}