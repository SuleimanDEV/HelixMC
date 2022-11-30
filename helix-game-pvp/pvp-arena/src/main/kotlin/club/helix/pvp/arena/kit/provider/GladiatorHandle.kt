package club.helix.pvp.arena.kit.provider

import club.helix.pvp.arena.PvPArena
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.File

class GladiatorHandle(private val plugin: PvPArena) {
    private val worldEditPlugin = Bukkit.getPluginManager().getPlugin("WorldEdit")
            as? WorldEditPlugin ?: throw NullPointerException("invalid world edit plugin")

    fun createBattle(p1: Player, p2: Player) {
        val mapGenerator = GladiatorMapGenerator(
            p1.location.clone(), worldEditPlugin, File(plugin.dataFolder, "gladiator.schematic")
        ).apply {
            Gladiator.players[GladiatorBattle(
                GladiatorBattle.GladiatorPlayer(p1, p1.location.clone()),
                GladiatorBattle.GladiatorPlayer(p2, p2.location.clone())
            )] = this
        }
        mapGenerator.load()

        p1.teleport(mapGenerator.center.clone().add(-5.0, 0.0, -5.0)
            .apply { yaw = -44.8f; pitch = -2.3f })
        p2.teleport(mapGenerator.center.clone().add(+6.0, 0.0, +6.0)
            .apply { yaw = 134.8f; pitch = 2.7f})
    }

    fun getGladiator(player: String) = Gladiator.players.entries.firstOrNull {
        it.key.contains(player)
    }

    fun inBattle(player: String) = Gladiator.players.keys.any {
        it.contains(player)
    }
}