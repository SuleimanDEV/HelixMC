package club.helix.bungeecord.shop

import club.helix.bungeecord.HelixBungee
import club.helix.components.HelixComponents
import club.helix.components.server.HelixServer
import net.md_5.bungee.api.ProxyServer
import java.util.concurrent.TimeUnit
import java.util.logging.Level

class CheckShopVipTask(private val plugin: HelixBungee) {

    private val shopApi = ShopVipAPI(plugin)

    fun run() = plugin.proxy.scheduler.schedule(plugin, {
        if (ProxyServer.getInstance().players.isEmpty()) return@schedule

        plugin.proxy.scheduler.runAsync(plugin) {
            plugin.logger.log(Level.INFO, "[SHOP] Verificando entregas...")
            val products = shopApi.get()

            products.filter {
                plugin.proxy.getPlayer(it.player) != null && HelixServer.getPlayerServer(it.player)
                    ?.let { server -> server.type != HelixServer.LOGIN } == true
            }.forEach { product ->
                plugin.logger.log(Level.INFO, "[SHOP] Ativando vip de ${product.player}...")
                shopApi.validProduct(product)

                plugin.components.userManager.getUser(product.player)?.let { user ->
                    product.apply(user)
                    plugin.components.userManager.saveAll(user)
                } ?: throw NullPointerException("invalid user account!")

                plugin.components.redisPool.resource.use {
                    it.publish("user-active-vip", HelixComponents.GSON.toJson(product))
                }
            }
        }
    }, 1, 2, TimeUnit.MINUTES)
}