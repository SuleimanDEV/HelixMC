package club.helix.lobby.provider.collectible

import club.helix.lobby.provider.ProviderLobby
import club.helix.lobby.provider.collectible.contraption.ContraptionCollectibles
import club.helix.lobby.provider.collectible.controller.CollectibleUserController
import club.helix.lobby.provider.collectible.hat.HatCollectibles
import club.helix.lobby.provider.collectible.listener.CollectibleContraptionInteractListener
import club.helix.lobby.provider.collectible.listener.CollectibleUserListener
import club.helix.lobby.provider.collectible.particle.ParticlesCollectibles
import org.bukkit.entity.Player
import org.bukkit.event.Listener

class CollectibleManager(val plugin: ProviderLobby) {

    private val collectibles = mutableListOf<Collectible>()
    private val users = mutableMapOf<String, MutableList<Collectible>>()
    val userController = CollectibleUserController(this)

    init {
        plugin.server.pluginManager.registerEvents(CollectibleUserListener(this), plugin)
        plugin.server.pluginManager.registerEvents(CollectibleContraptionInteractListener(this), plugin)

        collectibles.addAll(HatCollectibles.values().map { it.collectible })
        collectibles.addAll(ContraptionCollectibles.values().map { it.collectible })
        collectibles.addAll(ParticlesCollectibles.values().map { it.collectible })

        collectibles.filterIsInstance<Listener>().forEach {
            plugin.server.pluginManager.registerEvents(it, plugin)
        }
    }

    fun getCollectible(id: String) = getCollectibles().firstOrNull {
        it.id.lowercase() == id.lowercase()
    }

    fun set(player: Player, collectibles: MutableList<Collectible>) =
        users.put(player.name, collectibles)

    fun addCollectible(player: Player, collectible: Collectible) {
        clear(player, collectible.category)
        set(player, getSelectedCollectibles(player).apply { add(collectible) })
    }

    fun removeCollectible(player: Player, collectible: Collectible) {
        clear(player, collectible.category)
        val collectibles = getSelectedCollectibles(player).apply { remove(collectible) }

        if (collectibles.isNotEmpty()) {
            set(player, collectibles)
        }else users.remove(player.name)
    }

    private fun clear(player: Player, category: CollectibleCategory) =
        users[player.name]?.removeIf { it.category == category }

    inline fun <reified T: Collectible> getSelectedCollectible(player: Player, category: CollectibleCategory) = getSelectedCollectibles(player)
        .filterIsInstance<T>().firstOrNull { it.category == category }

    fun getSelectedCollectibles(player: Player) = users.getOrDefault(player.name, mutableListOf())
    fun hasSelectedCollectible(player: Player) = users.containsKey(player.name)

    fun hasSelectedCollectible(player: Player, collectible: Collectible) = users[player.name]?.any {
        it == collectible } == true

    fun hasSelectedCollectible(player: Player, category: CollectibleCategory) = users[player.name]?.any {
        it.category == category } == true

    fun getCollectibles() = collectibles.toList()

    fun getCollectibles(category: CollectibleCategory) = collectibles.filter {
        it.category == category
    }.toList()

    fun hasCollectiblePermission(player: Player, collectible: Collectible) =
        getCollectiblesPermission(player).contains(collectible)

    fun getCollectiblesPermission(player: Player) = collectibles.filter {
        player.hasPermission("helix.collectible.*") || player.hasPermission("helix.collectible.${it.id}")
    }.toList()

    fun getCollectiblesPermission(player: Player, category: CollectibleCategory) = collectibles.filter {
        getCollectiblesPermission(player).contains(it) && it.category == category
    }.toList()
}