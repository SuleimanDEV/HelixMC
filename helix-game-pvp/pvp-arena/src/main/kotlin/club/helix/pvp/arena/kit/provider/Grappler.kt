package club.helix.pvp.arena.kit.provider

import club.helix.bukkit.builder.ItemBuilder
import club.helix.bukkit.util.ReflectionUtil
import club.helix.pvp.arena.kit.KitHandler
import club.helix.pvp.arena.kotlin.player.allowedPvP
import club.helix.pvp.arena.kotlin.player.hasSelectedKit
import net.minecraft.server.v1_8_R3.EntityFishingHook
import net.minecraft.server.v1_8_R3.EntityHuman
import net.minecraft.server.v1_8_R3.EntitySnowball
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSnowball
import org.bukkit.entity.Entity
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class Grappler: KitHandler() {
    companion object {
        private val hook = mutableMapOf<String, Hook>()
    }

    override fun apply(player: Player): Unit = player.inventory.run {
        addItem(ItemBuilder()
            .type(Material.LEASH)
            .displayName("§aGrappler!")
            .identify("cancel-drop")
            .identify("kit", "grappler")
            .toItem
        )
    }

    @EventHandler fun onInteract(event: PlayerInteractEvent) = event.takeIf {
        it.player.allowedPvP && it.player.hasSelectedKit(this) && it.hasItem() && ItemBuilder.has(it.item, "kit", "grappler")
    }?.apply {
        if ((action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR)) {
            if (hook.containsKey(player.name)) {
                return@apply hook.remove(player.name)!!.die()
            }

            val hook = Hook(player.world,  (player as CraftPlayer).handle as EntityHuman)
            hook.spawn(player.location)
            hook.move(player.location.direction.x * 5.0, player.location.direction.y * 5.0, player.location.direction.z * 5.0)
        }else if ((action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) && hook.containsKey(player.name)) {
            val hook = hook[player.name]?.takeIf { it.isHook } ?: return@apply
            val hookLocation = hook.bukkitEntity.location
            val playerLocation = player.location
            val distance = hookLocation.distance(playerLocation)
            val near = distance < 1.5

            val vx = (distance + 1.0 * (1.0 * 0.04000000000000001) * if (near) 0.0 else (hookLocation.x - playerLocation.x)) / distance
            val vy = (distance * (0.9 + 0.3) * if (near) 0.5 else (hookLocation.y - playerLocation.y)) / distance
            val vz = (distance * (1.0 + 0.04000000000000001) * (if (near) 0.0 else hookLocation.z - playerLocation.z)) / distance
            player.velocity = player.velocity.setX(vx).setY(vy).setZ(vz).multiply(1.0)
        }
    }

    private class Hook(
        world: World,
        private val entityHuman: EntityHuman
    ): EntityFishingHook((world as CraftWorld).handle, entityHuman) {
        var isHook = false
        lateinit var fishHooked: Entity
        lateinit var entitySnowball: EntitySnowball

        fun spawn(location: Location) {
            val snowball = entityHuman.bukkitEntity.launchProjectile(Snowball::class.java)
            entitySnowball = (snowball as CraftSnowball).handle
            val packet = PacketPlayOutEntityDestroy(entitySnowball.id)

            Bukkit.getOnlinePlayers().forEach { ReflectionUtil.sendPacket(it, packet) }
            (location.world as CraftWorld).handle.addEntity(this)
        }

        override fun h() {
            if (entitySnowball.dead) return

            entitySnowball.world.world.entities.filter {
                it !is Firework && it !is Snowball && it.entityId != bukkitEntity.entityId &&
                        it.entityId != entityHuman.bukkitEntity.entityId &&
                        it.location.distance(entitySnowball.bukkitEntity.location) <= 2.0
            }.forEach {
                entityHuman.die()
                fishHooked = it
                isHook = false
                locX = it.location.x
                locY = it.location.y
                locZ = it.location.z
                motX = 0.0
                motY = 0.04
                motZ = 0.0
            }

            try {
                locX = fishHooked.location.x
                locY = fishHooked.location.y
                locZ = fishHooked.location.z
                motX = 0.0
                motY = 0.03
                motZ = 0.0
                isHook = true
            }catch (ignored: Exception) {
                if (entitySnowball.dead) {
                    isHook = true
                }
                locX = entitySnowball.locX
                locY = entitySnowball.locY
                locZ = entitySnowball.locZ
            }
        }
    }
}