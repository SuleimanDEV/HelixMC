package club.helix.duels.uhc.listener

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.kotlin.player.Build.Companion.build
import club.helix.duels.api.event.GameStartEvent
import club.helix.duels.api.game.DuelsPlayer
import club.helix.duels.uhc.UHCGame
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemFlag

class GameStartListener: Listener {

    @EventHandler fun onStart(event: GameStartEvent<UHCGame>) = event.game.apply {
        if (maxPlayers > 2) throw NullPointerException("invalid game players")

        val centerDistance = 25.0
        val pos1 = mapGenerator.location.clone().add(0.0, 0.0, -centerDistance)
            .apply { yaw = 0.5f; pitch = 0.5f }
        playingPlayers.first().player.teleport(pos1)

        val pos2 = mapGenerator.location.clone().add(0.0, 0.0, +centerDistance)
            .apply { yaw = 179.9f; pitch = -1.2f}
        playingPlayers[1].player.teleport(pos2)

        playingPlayers.map(DuelsPlayer::player).forEach {
            it.build(true)
            it.inventory.apply {
                clear()
                armorContents = null
                heldItemSlot = 0

                armorContents.apply {
                    helmet = ItemBuilder().type(Material.DIAMOND_HELMET)
                        .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItem
                    chestplate = ItemBuilder().type(Material.DIAMOND_CHESTPLATE)
                        .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItem
                    leggings = ItemBuilder().type(Material.DIAMOND_LEGGINGS)
                        .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItem
                    boots = ItemBuilder().type(Material.DIAMOND_BOOTS)
                        .enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItem
                }

                setItem(0, ItemBuilder().type(Material.DIAMOND_SWORD)
                    .enchantment(Enchantment.DAMAGE_ALL, 2)
                    .itemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
                    .toItem)
                setItem(1, ItemBuilder().type(Material.FISHING_ROD).unbreakable().toItem)
                setItem(2, ItemBuilder().type(Material.BOW)
                    .enchantment(Enchantment.ARROW_DAMAGE, 3).toItem)
                setItem(3, ItemBuilder().type(Material.DIAMOND_AXE).toItem)
                setItem(4, ItemBuilder().type(Material.GOLDEN_APPLE).amount(6).toItem)
                setItem(5, ItemBuilder().type(Material.GOLDEN_APPLE)
                    .displayName("§5Maçã Instantãnea")
                    .amount(2)
                    .identify("instant-apple").toItem)
                setItem(9, ItemBuilder().type(Material.ARROW).amount(16).toItem)

                ItemBuilder().type(Material.WOOD).amount(64).toItem.apply {
                    setItem(8, this); setItem(35, this) }

                ItemBuilder().type(Material.LAVA_BUCKET).toItem.apply {
                    setItem(6, this); setItem(33, this) }
                ItemBuilder().type(Material.WATER_BUCKET).toItem.apply {
                    setItem(7, this); setItem(34, this) }
            }
            it.gameMode = GameMode.SURVIVAL
        }
        broadcast("§aO jogo iniciou!")
    }
}