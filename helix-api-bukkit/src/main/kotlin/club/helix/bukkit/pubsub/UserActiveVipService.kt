package club.helix.bukkit.pubsub

import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.kotlin.player.sendPlayerTitle
import club.helix.bukkit.nms.NameTagNMS
import club.helix.components.HelixComponents
import club.helix.components.pubsub.PubSubService
import club.helix.components.shop.ShopVipProduct
import club.helix.components.util.HelixAddress
import org.bukkit.Bukkit
import org.bukkit.Sound

class UserActiveVipService(val components: HelixComponents): PubSubService("user-active-vip") {

    override fun onMessage(message: String) {
        val product = HelixComponents.GSON.fromJson(message, ShopVipProduct::class.java)
            ?: throw NullPointerException("product object is invalid!")
        val rankPrefix = product.rank.prefix.replace(" ", "")

        Bukkit.getOnlinePlayers().forEach {
            it.sendPlayerTitle("${product.rank.color}${product.player}", "Adquiriu rank $rankPrefix§f!", 4)
            it.sendMessage(arrayOf(
                "",
                "  ${product.rank.color}${product.player} §eadquiriu rank $rankPrefix§e!",
                "  §eAdquira você também: §d${HelixAddress.SHOP}",
                ""
            ))
        }

        Bukkit.getPlayer(product.player)?.apply {
            val user = account.apply {
                product.apply(this)
                components.userManager.saveAll(this)
            }
            NameTagNMS.setNametag(this, user.tag, NameTagNMS.Reason.TAG)

            playSound(location, Sound.LEVEL_UP, 50.0f, 50.0f)

            sendMessage(arrayOf(
                "  §a§lYAY! §aSeu vip foi ativado!",
                "  §aRank: $rankPrefix",
                "  §aDuração: ${product.getTimeFormatted()}",
                "",
            ))
        }
    }
}