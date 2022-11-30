package club.helix.components.fetcher

import club.helix.components.HelixComponents
import com.google.gson.JsonObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.regex.Pattern

class MojangFetcher {
    companion object {
        private const val uuidURL = "https://api.mojang.com/users/profiles/minecraft/%s"
        private const val premiumEndpoint = "https://api.mojang.com/users/profiles/minecraft/%s"
            private val uuidPattern = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})")

        fun isPremium(name: String): Boolean = (URL(String.format(premiumEndpoint, name)).openConnection() as HttpURLConnection).let { connection ->
            connection.apply {
                connectTimeout = 1000
                readTimeout = 1000
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("User-Agent", "Premium-Checker")
            }
            val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
            val result = bufferedReader.readLine().apply { connection.disconnect() }

            return result?.let { it != "null" } == true
        }

        fun getUUID(name: String): UUID? {
            try {
                val url = URL(String.format(uuidURL, name))
                val urlConnection = url.openConnection()
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.getInputStream()))
                val json = bufferedReader.readLines().joinToString(" ").takeIf { it.isNotEmpty() } ?: return null

                val jsonObject = HelixComponents.GSON.fromJson(json, JsonObject::class.java)

                println(jsonObject)
                return jsonObject.get("id").asString?.let {
                    UUID.fromString(uuidPattern.matcher(it).replaceAll("$1-$2-$3-$4-$5"))
                }
            }catch (error: Exception) {
                error.printStackTrace()
            }
            return null
        }
    }
}