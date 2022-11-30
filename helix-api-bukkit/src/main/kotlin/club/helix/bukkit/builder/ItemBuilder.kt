package club.helix.bukkit.builder

import club.helix.bukkit.util.ReflectionUtil
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Material
import org.bukkit.SkullType
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

class ItemBuilder() {
    companion object {
        fun set(item: ItemStack, key: String, data: String = ""): ItemStack = CraftItemStack.asNMSCopy(item).let {
            CraftItemStack.asBukkitCopy(it.apply { this.tag.setString(key, data) })
        }

        fun get(item: ItemStack, key: String): String? = CraftItemStack.asNMSCopy(item)?.tag?.getString(key)

        fun has(item: ItemStack, key: String): Boolean = CraftItemStack.asNMSCopy(item)?.tag?.hasKey(key) == true

        fun has(item: ItemStack, key: String, data: String) = get(item, key)?.equals(data) == true
    }

    constructor(itemStack: ItemStack): this() {
        this.itemStack = itemStack
    }

    private var itemStack: ItemStack = ItemStack(Material.AIR)

    fun skull(owner: String) = this.apply {
        this.type(Material.SKULL_ITEM)
        this.data(SkullType.PLAYER.ordinal)
        this.itemStack.itemMeta = (itemStack.itemMeta as SkullMeta).apply { this.owner = owner }
    }

    fun customSkull(url: String) = this.apply {
        this.type(Material.SKULL_ITEM)
        this.data(3)

        val itemMeta = (this.itemStack.itemMeta as SkullMeta)
        val profile = GameProfile(UUID.randomUUID(), null)

        profile.properties.put("textures", Property("textures", url))
        ReflectionUtil.setField(itemMeta, itemMeta.javaClass.getDeclaredField("profile"), profile)

        this.itemStack.itemMeta = itemMeta
    }

    fun type(material: Material) = this.apply {
        this.itemStack.type = material
    }

    fun data(value: Int) = this.apply {
        val itemMeta = this.itemStack.itemMeta.clone()

        this.itemStack = ItemStack(itemStack.type, itemStack.amount, itemStack.durability, value.toByte())
        this.itemStack.itemMeta = itemMeta
    }

    fun displayName(value: String) = this.apply {
        this.itemStack.itemMeta = itemStack.itemMeta.apply { this.displayName = value }
    }

    fun lore(vararg value: String) = this.apply {
        this.itemStack.itemMeta = itemStack.itemMeta.apply { this.lore = value.toList() }
    }

    fun itemFlags(vararg value: ItemFlag) = this.apply {
        this.itemStack.itemMeta = itemStack.itemMeta.apply { this.addItemFlags(*value) }
    }

    fun enchantment(enchantment: Enchantment, level: Int) = this.apply {
        this.itemStack.itemMeta = itemStack.itemMeta.apply {
            this.addEnchant(enchantment, level, true)
        }
    }

    fun durability(value: Int) = this.apply {
        this.itemStack.durability = value.toShort()
    }

    fun unbreakable() = this.apply {
        this.itemStack.itemMeta = itemStack.itemMeta.apply {
            spigot().isUnbreakable = true
        }
    }

    fun amount(value: Int) = this.apply { this.itemStack.amount = value }

    fun identify(key: String, data: String = "") = this.apply {
        this.itemStack = set(itemStack, key, data)
    }

    val toItem get(): ItemStack = this.itemStack

    fun clone() = ItemBuilder(ItemStack(itemStack.type, itemStack.amount, itemStack.data.data.toShort()).apply {
        this.itemMeta = this@ItemBuilder.itemStack.itemMeta
    })
}