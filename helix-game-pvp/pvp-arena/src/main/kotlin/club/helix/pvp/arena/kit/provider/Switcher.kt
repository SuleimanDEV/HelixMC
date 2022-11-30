package club.helix.pvp.arena.kit.provider

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.kit.KitHandlerCooldown
import club.helix.pvp.arena.kotlin.player.allowedPvP
import club.helix.pvp.arena.kotlin.player.hasSelectedKit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.metadata.FixedMetadataValue

class Switcher: KitHandlerCooldown("switcher-kit", 6) {

    override fun apply(player: Player): Unit = player.inventory.run {
        addItem(ItemBuilder()
            .type(Material.SNOW_BALL)
            .displayName("§aSwitcher!")
            .identify("cancel-drop")
            .identify("kit", "switcher")
            .toItem
        )
    }

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        valid(it.player) && event.hasItem() && ItemBuilder.has(event.item, "kit", "switcher")
    }?.apply {
        isCancelled = true
        player.updateInventory()

        if (hasCooldownOrCreate(player)) return@apply sendCooldownMessage(player)
        player.launchProjectile(Snowball::class.java).setMetadata("switcher", FixedMetadataValue(plugin, ""))
    }

    @EventHandler fun onDamageBySnowball(event: EntityDamageByEntityEvent) = event.takeIf {
        (it.damager as? Snowball)?.let { snowball -> snowball.hasMetadata("switcher") && snowball.shooter is Player && (snowball.shooter as Player).allowedPvP && (snowball.shooter as Player).hasSelectedKit(this) } == true
                && it.entity is Player && (it.entity as Player).allowedPvP
    }?.apply {
        val entityLocation = entity.location.clone()
        val shooter = (damager as Snowball).shooter as Player
        val shooterLocation = shooter.location.clone()
        shooter.teleport(entityLocation)
        entity.teleport(shooterLocation)

        entity.sendMessage("§a[Switcher] ${shooter.name} trocou de lugar com você.")
        shooter.sendMessage("§a[Switcher] Você trocou de lugar com ${entity.name}.")
    }
}