package club.helix.pvp.arena.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.kotlin.player.allowedPvP
import club.helix.pvp.arena.kotlin.player.arenaPlayer
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemFlag

class UserJoinPvPListener(private val plugin: PvPArena): Listener {

    private fun execute(player: Player) = player.apply {
        maxHealth = 20.0
        health = 20.0
        foodLevel = 20
        inventory.clear()
        inventory.armorContents = null
        activePotionEffects.forEach { removePotionEffect(it.type) }
        player.fallDistance = -1f

        inventory.apply {
            setItem(0, ItemBuilder()
                .type(Material.STONE_SWORD)
                .displayName("§7Espada de Pedra")
                .unbreakable()
                .itemFlags(*ItemFlag.values())
                .identify("cancel-drop")
                .toItem
            )
            setItem(13, ItemBuilder().type(Material.BOWL).amount(32).toItem)
            setItem(14, ItemBuilder().type(Material.RED_MUSHROOM).amount(32).toItem)
            setItem(15, ItemBuilder().type(Material.BROWN_MUSHROOM).amount(32).toItem)

            setItem(8, ItemBuilder()
                .type(Material.COMPASS)
                .displayName("§aLocalizador")
                .identify("cancel-drop")
                .identify("find-compass-player")
                .toItem
            )
            player.arenaPlayer.apply {
                pvp = true
                selectedKits.values.forEach { it.handler?.apply(player) }
            }

            for (i in 0..35) {
                addItem(ItemBuilder().type(Material.MUSHROOM_SOUP).toItem)
            }
        }
        player.closeInventory()
        player.sendMessage("§cVocê perdeu a proteção do spawn.")
    }

    @EventHandler fun onDamage(event: EntityDamageEvent) = event.takeIf {
        (it.entity as? Player)?.let { entity ->
            it.cause == EntityDamageEvent.DamageCause.FALL
                    && entity.gameMode != GameMode.CREATIVE && !entity.allowedPvP
                    && entity.location.y <= (plugin.serverSpawnHandle.spawnLocation.y - 10)
        } == true
    }?.apply {
        event.isCancelled = true
        execute(entity as Player)
    }

    @EventHandler fun onMoveEvent(event: PlayerMoveEvent) = event.takeIf {
        it.player.gameMode != GameMode.CREATIVE && !it.player.allowedPvP &&
                it.player.location.block.getRelative(BlockFace.DOWN).type != Material.AIR &&
                it.player.location.y <= (plugin.serverSpawnHandle.spawnLocation.y - 10)
    }?.apply { execute(player) }
}