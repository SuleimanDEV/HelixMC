package club.helix.pvp.arena.feast

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.builder.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.concurrent.TimeUnit

class FeastRunnable(
    private val spawnEveryMinutes: Int = 8,
    private val spawnDurationSeconds: Int = 18
): BukkitRunnable() {

    private val world get() = HelixBukkit.instance.world
    val center get() = Location(world, -11.0, 66.0, 34.0)
    var spawned: Boolean = false
        private set

    private val chestItemMinAmount = 2
    private val chestItemMaxAmount = 3

    private val chests get() = mutableListOf(
        Location(world, -11.0, 66.0, 32.0),
        Location(world, -10.0, 66.0, 33.0),
        Location(world, -9.0, 66.0, 34.0),
        Location(world, -10.0, 66.0, 35.0),
        Location(world, -11.0, 66.0, 36.0),
        Location(world, -12.0, 66.0, 35.0),
        Location(world, -13.0, 66.0, 34.0),
        Location(world, -12.0, 66.0, 33.0)
    )

    private val items = arrayOf(
        ItemBuilder().type(Material.FISHING_ROD).toItem,
        ItemBuilder().type(Material.GOLDEN_APPLE).toItem,
        ItemBuilder().type(Material.GOLDEN_APPLE).amount(2).toItem,
        ItemBuilder().type(Material.CHAINMAIL_BOOTS).toItem,
        ItemBuilder().type(Material.LEATHER_CHESTPLATE).toItem,
        ItemBuilder().type(Material.LEATHER_BOOTS).toItem,
        ItemBuilder().type(Material.IRON_BOOTS).durability(Material.IRON_BOOTS.maxDurability - 6).toItem,
        ItemBuilder().type(Material.ENDER_PEARL).amount(3).toItem,
        ItemBuilder().type(Material.ENDER_PEARL).amount(1).toItem,
        ItemBuilder().type(Material.EXP_BOTTLE).amount(2).toItem,
        ItemBuilder().type(Material.SNOW_BALL).amount(19).toItem,
        ItemBuilder().type(Material.SNOW_BALL).amount(5).toItem,
        ItemBuilder().type(Material.SNOW_BALL).amount(3).toItem,
        ItemBuilder().type(Material.POTION).data(16386).toItem,
        ItemBuilder().type(Material.POTION).data(16389).toItem,
        ItemBuilder().type(Material.POTION).data(16427).toItem,
        ItemBuilder().type(Material.POTION).data(16419).toItem,
        ItemBuilder().type(Material.POTION).data(16420).toItem,
        ItemBuilder().type(Material.INK_SACK).data(3).amount(24).toItem,
        ItemBuilder().type(Material.TNT).displayName("§cTnt Prime").identify("tnt-primed").toItem,
        ItemBuilder().type(Material.TNT).displayName("§cTnt Prime").identify("tnt-primed").toItem,
        ItemBuilder().type(Material.GOLDEN_APPLE).enchantment(Enchantment.DAMAGE_ALL, 1).toItem,
        ItemBuilder().type(Material.WOOD_SWORD)
            .enchantment(Enchantment.DAMAGE_ALL, 2)
            .durability(Material.WOOD_SWORD.maxDurability.toInt())
            .itemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
            .toItem,
        ItemBuilder().type(Material.GOLD_SWORD)
            .enchantment(Enchantment.KNOCKBACK, 1)
            .enchantment(Enchantment.FIRE_ASPECT, 1)
            .durability(Material.GOLD_SWORD.maxDurability.toInt())
            .itemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
            .toItem,
    )

    var time = TimeUnit.MINUTES.toSeconds(spawnEveryMinutes.toLong()).toInt()
        private set

    override fun run() {
        if (Bukkit.getOnlinePlayers().isEmpty() && !spawned) return
        time--
        val minutes = time / 60
        val seconds = time % 60

        if (!spawned && minutes == 0) {
            if (seconds == 50 || seconds == 30 || seconds == 20 || seconds == 10 || seconds == 3) {
                Bukkit.broadcastMessage("§dO feast irá nascer em §f$seconds §d${if (seconds > 1) "segundos" else "segundo"}.")
            }

            if (seconds <= 0) {
                Bukkit.broadcastMessage("§dO feast nasceu!")
                time = spawnDurationSeconds
                spawned = true
                center.world.strikeLightningEffect(center)

                return chests.forEach {
                    it.block.type = Material.CHEST
                    val random = Random()

                    (it.block.state as Chest).inventory.apply {
                        val itemsAmount = random.nextInt(chestItemMaxAmount + 1 - chestItemMinAmount) + chestItemMaxAmount

                        for (i in 0 until itemsAmount) {
                            val slot = random.nextInt(size)
                            setItem(slot, items.random())
                        }
                    }
                }
            }
        }

        if (spawned && seconds <= 0) {
            time = TimeUnit.MINUTES.toSeconds(spawnEveryMinutes.toLong()).toInt()
            chests.map(Location::getBlock).forEach {
                (it.state as? Chest)?.inventory?.clear()
                it.type = Material.AIR
            }
            spawned = false
        }
    }
}