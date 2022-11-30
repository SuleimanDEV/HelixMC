package club.helix.bukkit.listener

import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffectType

class UserConfigureDamageListener: Listener {

    enum class SwordBase(val damage: Int) {
        WOODEN_SWORD(2),
        GOLDEN_SWORD(2),
        STONE_SWORD(3),
        IRON_SWORD(4),
        DIAMOND_SWORD(6)
    }

    enum class ArmorBase(val protection: Double) {
        LEATHER_HELMET(-0.420),
        LEATHER_CHESTPLATE(-1.260),
        LEATHER_LEGGINGS(-0.840),
        LEATHER_BOOTS(0.420),

        IRON_HELMET(-0.840),
        IRON_CHESTPLATE(-2.519),
        IRON_LEGGINGS(-2.100),
        IRON_BOOTS(-0.840),

        DIAMOND_HELMET(-0.940),
        DIAMOND_CHESTPLATE(-2.619),
        DIAMONDIRON_LEGGINGS(-2.200),
        DIAMOND_BOOTS(-0.940),
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) = event.takeIf {
        it.damager is Player
    }?.apply {
        val damager = damager as Player
        var base = SwordBase.values().firstOrNull { it.toString() == damager.itemInHand.type.toString() }?.damage
            ?: 1

        val sharpness = damager.itemInHand.getEnchantmentLevel(Enchantment.DAMAGE_ALL)
        val strength = damager.activePotionEffects.firstOrNull {
            it.type == PotionEffectType.INCREASE_DAMAGE
        }?.amplifier?.inc() ?: 0

        base += sharpness * 1
        base += strength * 1
        damage = base.toDouble()

        (entity as? LivingEntity)?.equipment?.apply {
            val helmetProtection = ArmorBase.values().firstOrNull {
                helmet?.type?.let { material -> material.toString() == it.toString() } == true
            }?.protection ?: 0.0
            val chestplateProtection = ArmorBase.values().firstOrNull {
                chestplate?.type?.let { material -> material.toString() == it.toString() } == true
            }?.protection ?: 0.0
            val leggingsProtection = ArmorBase.values().firstOrNull {
                leggings?.type?.let { material -> material.toString() == it.toString() } == true
            }?.protection ?: 0.0
            val bootsProtection = ArmorBase.values().firstOrNull {
                boots?.type?.let { material -> material.toString() == it.toString() } == true
            }?.protection ?: 0.0
            if (isApplicable(EntityDamageEvent.DamageModifier.ARMOR)) {
                setDamage(EntityDamageEvent.DamageModifier.ARMOR, helmetProtection + chestplateProtection +
                        leggingsProtection + bootsProtection)
            }
        }

        if (isApplicable(EntityDamageEvent.DamageModifier.BLOCKING)) {
            setDamage(EntityDamageEvent.DamageModifier.BLOCKING, 0.0)
        }
        if (isApplicable(EntityDamageEvent.DamageModifier.BASE)) {
            setDamage(EntityDamageEvent.DamageModifier.BASE, base.toDouble())
        }
    }
}