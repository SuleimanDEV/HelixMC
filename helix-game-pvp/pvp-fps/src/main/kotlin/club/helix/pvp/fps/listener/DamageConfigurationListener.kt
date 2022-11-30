package club.helix.pvp.fps.listener

import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class DamageConfigurationListener: Listener {

    private enum class Sword(val damage: Int) {
        WOODEN_SWORD(1),
        GOLDEN_SWORD(1),
        STONE_SWORD(3),
        IRON_SWORD(4),
        DIAMOND_SWORD(6);
    }

    private enum class Armor(val protection: Double) {
        HELMET(0.2),
        CHESTEPLATE(0.4),
        LEGGINGS(0.3),
        BOOTS(0.2);
    }

    @EventHandler (priority = EventPriority.HIGH)
    fun swordDamage(event: EntityDamageByEntityEvent) = event.takeIf {
        it.damager is Player && (it.damager as Player).itemInHand != null
    }?.apply {
        val player = damager as Player
        val sword = Sword.values().firstOrNull { it.toString() == player.itemInHand.type.toString() } ?: return@apply
        var sharpnessDamage = 0.0

        player.itemInHand.getEnchantmentLevel(Enchantment.DAMAGE_ALL).takeIf { it != 0 }?.apply {
            for (i in 0 until this) {
                sharpnessDamage += 1
            }
        }
        damage = sword.damage.toDouble() + sharpnessDamage
    }

    @EventHandler (priority = EventPriority.LOW)
    fun armorProtection(event: EntityDamageByEntityEvent) = event.takeIf {
        it.damager is Player && (it.damager as Player).itemInHand != null
    }?.apply {
        val inventory = (damager as Player).inventory

        if (inventory.helmet == null) damage += Armor.HELMET.protection
        if (inventory.chestplate == null) damage += Armor.CHESTEPLATE.protection
        if (inventory.leggings == null) damage += Armor.LEGGINGS.protection
        if (inventory.boots == null) damage += Armor.BOOTS.protection
    }
}