package club.helix.duels.gladiator.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.Build.Companion.build
import club.helix.duels.api.event.GameStartEvent
import club.helix.duels.api.game.DuelsPlayer
import club.helix.duels.gladiator.GladiatorGame
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class GameStartListener: Listener {

    @EventHandler fun onStart(event: GameStartEvent<GladiatorGame>) = event.game.apply {
        if (maxPlayers > 2) throw NullPointerException("invalid game players")

        val pos1 = mapGenerator.location.clone().add(-5.0, 0.0, -5.0)
            .apply { yaw = -44.8f; pitch = -2.3f }
        playingPlayers.first().player.teleport(pos1)

        val pos2 = mapGenerator.location.clone().add(+6.0, 0.0, +6.0)
            .apply { yaw = 134.8f; pitch = 2.7f}
        playingPlayers[1].player.teleport(pos2)

        playingPlayers.map(DuelsPlayer::player).forEach {
            it.build(true)
            it.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3 * 20, 4))

            it.inventory.apply {
                clear()
                armorContents = null

                ItemBuilder().type(Material.IRON_HELMET).toItem.apply {
                    helmet = this; setItem(9, this); setItem(18, this) }

                ItemBuilder().type(Material.IRON_CHESTPLATE).toItem.apply {
                    chestplate = this; setItem(10, this); setItem(19, this) }

                ItemBuilder().type(Material.IRON_LEGGINGS).toItem.apply {
                    leggings = this; setItem(11, this); setItem(20, this) }

                ItemBuilder().type(Material.IRON_BOOTS).toItem.apply {
                    boots = this; setItem(12, this); setItem(21, this) }

                ItemBuilder().type(Material.LAVA_BUCKET).toItem.apply {
                    setItem(27, this); setItem(28, this); setItem(2, this) }

                ItemBuilder().type(Material.BOWL).amount(64).toItem.apply {
                    setItem(13, this); setItem(22, this) }

                ItemBuilder().type(Material.INK_SACK).data(3).amount(64).toItem.apply {
                    setItem(14, this); setItem(15, this); setItem(16, this)
                    setItem(23, this); setItem(24, this); setItem(25, this)
                }

                setItem(0, ItemBuilder().type(Material.DIAMOND_SWORD)
                    .enchantment(Enchantment.DAMAGE_ALL, 3).toItem)
                setItem(1, ItemBuilder().type(Material.WATER_BUCKET).toItem)
                setItem(3, ItemBuilder().type(Material.WOOD).amount(64).toItem)
                setItem(8, ItemBuilder().type(Material.COBBLE_WALL).amount(64).toItem)
                setItem(17, ItemBuilder().type(Material.STONE_AXE).toItem)
                setItem(26, ItemBuilder().type(Material.STONE_PICKAXE).toItem)

                for (i in 0..10) {
                    addItem(ItemBuilder().type(Material.MUSHROOM_SOUP).toItem)
                }
            }
            it.gameMode = GameMode.SURVIVAL
        }
        broadcast("§aO jogo iniciou!")
    }
}