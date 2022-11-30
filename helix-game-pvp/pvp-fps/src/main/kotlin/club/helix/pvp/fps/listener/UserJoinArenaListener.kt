package club.helix.pvp.fps.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.fps.PvPFps
import club.helix.pvp.fps.kotlin.player.addPvP
import club.helix.pvp.fps.kotlin.player.allowedPvP
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemFlag

class UserJoinArenaListener(private val plugin: PvPFps): Listener {

    private fun execute(player: Player) = player.apply {
        addPvP()
        fallDistance = -1f
        maxHealth = 20.0
        health = maxHealth
        foodLevel = 20
        inventory.clear()
        inventory.armorContents = null
        activePotionEffects.forEach { removePotionEffect(it.type) }
        inventory.heldItemSlot = 0

        inventory.apply {
            setItem(0, ItemBuilder()
                .type(Material.DIAMOND_SWORD)
                .displayName("§bEspada de Diamante")
                .enchantment(Enchantment.DAMAGE_ALL, 2)
                .identify("cancel-drop")
                .unbreakable()
                .itemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
                .toItem
            )
            helmet = ItemBuilder().type(Material.IRON_HELMET).toItem
            chestplate = ItemBuilder().type(Material.IRON_CHESTPLATE).toItem
            leggings = ItemBuilder().type(Material.IRON_LEGGINGS).toItem
            boots = ItemBuilder().type(Material.IRON_BOOTS).toItem

            setItem(13, ItemBuilder().type(Material.BOWL).amount(64).toItem)
            setItem(14, ItemBuilder().type(Material.RED_MUSHROOM).amount(64).toItem)
            setItem(15, ItemBuilder().type(Material.BROWN_MUSHROOM).amount(64).toItem)

            for (i in 0..35) {
                addItem(ItemBuilder().type(Material.MUSHROOM_SOUP).toItem)
            }
        }
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
                it.player.location.y <= (plugin.serverSpawnHandle.spawnLocation.y - 2)
    }?.apply { execute(player) }
}