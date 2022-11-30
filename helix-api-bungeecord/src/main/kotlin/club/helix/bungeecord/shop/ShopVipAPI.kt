package club.helix.bungeecord.shop

import club.helix.bungeecord.HelixBungee
import club.helix.components.HelixComponents
import club.helix.components.shop.ShopVipProduct
import com.google.gson.JsonArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.logging.Level

class ShopVipAPI(private val plugin: HelixBungee) {
    companion object {
        private const val URL = "https://api.lojasquare.net/v1"
        private const val ENDPOINT_DELIVERIES = "/entregas/1"
        private const val ENDPOINT_DELIVER = "/entregas/%s/entregue"
        private const val TOKEN = "qm8mFkTn96wc2AkYTtPV7FpFfCu4ce"
    }

    fun validProduct(product: ShopVipProduct) {
        try {
            val httpURLConnection = (URL(String.format("$URL$ENDPOINT_DELIVER", product.id)).openConnection() as HttpURLConnection).apply {
                requestMethod = "PUT"
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)")
                setRequestProperty("Authorization", TOKEN)
                useCaches = false
                allowUserInteraction = false
            }
            httpURLConnection.responseCode.takeIf { it != 200 }?.let {
                plugin.logger.log(Level.SEVERE, "[SHOP] Ocorreu um erro ao validar a entrega ${product.id}")
                return plugin.logger.log(Level.SEVERE, "[SHOP] Response Code: $it")
            }
            plugin.logger.log(Level.INFO, "[SHOP] ${product.id} foi marcado como entregue!")
        }catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun get() = mutableListOf<ShopVipProduct>().apply {
        try {
            val httpUrlConnection = (URL("$URL$ENDPOINT_DELIVERIES").openConnection() as HttpURLConnection).apply {
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)")
                setRequestProperty("Authorization", TOKEN)
                useCaches = false
                allowUserInteraction = false
            }

            httpUrlConnection.responseCode.takeIf { it != 200 }?.let {
                plugin.logger.log(Level.SEVERE, "[SHOP] Ocorreu um erro pegar as entregas.")
                return@apply plugin.logger.log(Level.SEVERE, "[SHOP] Response Code: $it")
            }

            val bufferedReader = BufferedReader(InputStreamReader(httpUrlConnection.inputStream))
            val jsonMessage = bufferedReader.readLines().joinToString(" ")
            plugin.logger.log(Level.INFO, "[SHOP] Received: $jsonMessage")

            val jsonArray = HelixComponents.GSON.fromJson(jsonMessage, JsonArray::class.java)
                ?: throw NullPointerException("invalid json array")

            plugin.logger.log(Level.INFO, "[SHOP] Receiving ${jsonArray.size()} products...")
            jsonArray.forEach {
                val jsonObject = it.asJsonObject
                    ?: throw NullPointerException("invalid json object")
                add(ShopVipProduct(
                    jsonObject.get("entregaID").asString?.toIntOrNull()
                        ?: throw NullPointerException("invalid id"),
                    jsonObject.get("player").asString
                        ?: throw NullPointerException("invalid player"),
                    jsonObject.get("produto").asString
                        ?: throw NullPointerException("invalid product"),
                    jsonObject.get("quantidade").asString?.toIntOrNull()
                        ?: throw NullPointerException("invalid amount"),
                    jsonObject.get("servidor").asString
                        ?: throw NullPointerException("invalid server"),
                    jsonObject.get("subServidor").asString
                        ?: throw NullPointerException("invalid sub server"),
                    jsonObject.get("grupo").asString
                        ?: throw NullPointerException("invalid group"),
                    jsonObject.get("dias").asString?.toIntOrNull()
                        ?: throw NullPointerException("invalid days"),
                    jsonObject.get("codigo").asString
                        ?: throw NullPointerException("invalid code"),
                ))
            }
        }catch (error: Exception) {
            error.printStackTrace()
        }
    }
}