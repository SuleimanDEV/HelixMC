package club.helix.components.server

import club.helix.components.server.provider.HardcoreGamesProvider
import club.helix.components.server.provider.LobbyProvider

enum class HelixServer(
    val displayName: String,
    val providers: MutableMap<String, HelixServerProvider>
) {

    LOGIN("Login", mutableMapOf(
        LobbyProvider("Login", "login-1").toPair()
    )),

    MAIN_LOBBY("Lobby Principal", mutableMapOf(
        LobbyProvider("Lobby Principal #1", "main-lobby-1").toPair(),
        LobbyProvider("Lobby Principal #2", "main-lobby-2").toPair()
    )),

    HG("HG", mutableMapOf(
        LobbyProvider("HG Lobby #1", "hg-lobby-1").toPair(),
        LobbyProvider("HG Lobby #2", "hg-lobby-2").toPair(),
        HardcoreGamesProvider(Category.HG_MIX, "HG Mix #1", "hg-mix-1").toPair()
    )),

    DUELS("Duels", mutableMapOf(
        LobbyProvider("Duels Lobby #1", "duels-lobby-1").toPair(),
        HelixServerProvider(Category.DUELS_SOUP, "Duels: Sopa", "duels-soup-1").toPair(),
        HelixServerProvider(Category.DUELS_LAVA, "Duels: Lava", "duels-lava-1").toPair(),
        HelixServerProvider(Category.DUELS_SUMO, "Duels: Sumo", "duels-sumo-1").toPair(),
        HelixServerProvider(Category.DUELS_UHC, "Duels: UHC", "duels-uhc-1").toPair(),
        HelixServerProvider(Category.DUELS_GLADIATOR, "Duels: Gladiator", "duels-gladiator-1").toPair()
    )),

    PVP("PvP", mutableMapOf(
        LobbyProvider("PvP Lobby #1", "pvp-lobby-1").toPair(),
        HelixServerProvider(Category.PVP_ARENA, "PvP: Arena", "pvp-arena-1").toPair(),
        HelixServerProvider(Category.PVP_FPS, "PvP: Fps", "pvp-fps-1").toPair(),
        HelixServerProvider(Category.PVP_LAVA, "PvP: Lava", "pvp-lava-1").toPair()
    )),

    SPECIAL("Especial", mutableMapOf(
        HelixServerProvider(Category.EVENT, "Evento", "event").toPair()
    ));

    enum class Category {
        LOBBY, HG_MIX, DUELS_LAVA, DUELS_SOUP, DUELS_SUMO, DUELS_UHC, DUELS_GLADIATOR,
        PVP_ARENA, PVP_FPS, PVP_LAVA, EVENT; }

    init { loadAttributes() }

    companion object {
        val networkPlayers get() = mutableListOf<String>().apply {
            values().forEach { addAll(it.onlinePlayers) }
        }

        fun networkOnline(name: String) = networkPlayers.any { name.lowercase() == it.lowercase() }

        fun getPlayerServer(name: String): HelixServerProvider? = values().flatMap { it.providers.values }.firstOrNull {
            it.onlinePlayers.any { player -> player.lowercase() == name.lowercase() }
        }

        fun getServer(name: String): HelixServerProvider? = values().flatMap { it.providers.values }.firstOrNull {
            it.serverName.lowercase() == name.lowercase()
        }

        fun register(type: String, categoryName: String, number: Int) = values().firstOrNull { it.toString().lowercase() == type.lowercase() }?.let {
            if (number == 0)
                throw NullPointerException("Quantidade de subservers de $it: 1 - ${it.providers.size}")

            val category = Category.values().firstOrNull { category -> category.toString() == categoryName }
                ?: throw NullPointerException("categoria não encontrada")

            it.providers(category)[number - 1]
        } ?: throw NullPointerException("servidor não encontrado")
    }

    val onlinePlayers get() = mutableListOf<String>().apply {
        providers.values.forEach { addAll(it.onlinePlayers) }
    }

    fun onlinePlayers(category: Category) = mutableListOf<String>().apply {
        providers.values.filter { it.category == category }.forEach { addAll(it.onlinePlayers) }
    }

    val single = providers.size == 1

    fun getServer(serverName: String) = providers.values.firstOrNull { it.serverName == serverName }

    fun updateProvider(provider: HelixServerProvider) =
        providers.put(provider.serverName, provider)

    private fun loadAttributes(): Unit = Category.values().forEach { category ->
        val providers = providers.values.filter { it.category == category }
        for (i in providers.indices) {
            providers[i].apply {
                type = this@HelixServer
                id = i.plus(1)
            }
        }
    }

    fun getPlayer(name: String) = onlinePlayers.firstOrNull {
        it == name }

    fun providers(category: Category) = providers.values.filter { it.category == category }
    @JvmName("providersCast")
    inline fun <reified T: HelixServerProvider> providers(category: Category) =
        providers.values.filterIsInstance<T>().filter { it.category == category }

    fun findAvailable() = findAvailableList()?.firstOrNull()

    private fun findAvailableList() = providers.values.filter(HelixServerProvider::available).sortedBy { it.onlinePlayers.size }.takeIf { it.isNotEmpty() }
    fun findAvailable(category: Category) = findAvailableList()?.firstOrNull { it.category == category }
}