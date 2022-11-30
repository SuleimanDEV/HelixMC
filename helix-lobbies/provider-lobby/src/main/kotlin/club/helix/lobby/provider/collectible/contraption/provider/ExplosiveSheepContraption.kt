package club.helix.lobby.provider.collectible.contraption.provider

import club.helix.bukkit.builder.ItemBuilder
import club.helix.lobby.provider.ProviderLobby
import club.helix.lobby.provider.collectible.contraption.ContraptionCollectible
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Sheep
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable

class ExplosiveSheepContraption: Listener, ContraptionCollectible(
    "explosive-sheep-contraption",
    "Ovelha Explosiva",
    25,
    ItemStack(Material.TNT),
    1640217525682
) {

    private var entityDurationTicks: Long = 4 * 20
    private var runnableTicks: Long = 2
    private var explosionRadius: Float = 3.5f
    private var entityMetadata = "explosive_sheep"

    override var item = ItemBuilder()
        .type(Material.TNT)
        .displayName("§aOvelha Explosiva")
        .identify("cancel-drop")
        .identify("cancel-click")
        .toItem

    override fun onInteract(player: Player) {
        val plugin = ProviderLobby.instance

        val sheep = player.world.spawn(player.eyeLocation, Sheep::class.java).apply {
            isCustomNameVisible = true
            noDamageTicks = Integer.MAX_VALUE
            isSheared = false
        }
        sheep.setMetadata(entityMetadata, FixedMetadataValue(plugin, player.name))

        var time = entityDurationTicks

        object: BukkitRunnable() {
            override fun run() {
                if (time <= 0) {
                    if (!sheep.isDead && sheep.isValid) {
                        sheep.world.createExplosion(sheep.location.x, sheep.location.y, sheep.location.z, explosionRadius, true, false)
                        sheep.remove()
                    }
                    return cancel()
                }
                sheep.color = DyeColor.values().random()
                sheep.customName = "§c${time.toDouble() / 20}s"

                time -= runnableTicks
            }
        }.runTaskTimer(plugin, 0, runnableTicks)
    }

    override fun onDisable(player: Player) {
        super.onDisable(player)
        player.world.entities.filter { it.hasMetadata(entityMetadata) }.forEach(Entity::remove)
    }
}