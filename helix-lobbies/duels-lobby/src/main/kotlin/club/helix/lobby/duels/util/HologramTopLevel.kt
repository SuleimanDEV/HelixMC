package club.helix.lobby.duels.util

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.hologram.HelixHologram
import club.helix.components.HelixComponents
import club.helix.components.account.HelixUser
import club.helix.components.account.game.DuelsLevel
import club.helix.lobby.duels.DuelsLobby
import org.bukkit.Bukkit
import org.bukkit.Location

class HologramTopLevel(private val plugin: DuelsLobby) {

    private val hologram = HelixHologram(Location(
        HelixBukkit.instance.world, -8.523580661902106, 61.6, 13.025585409959856, 270.7486f, 0.29995385f
    ), "§d§lTOP 10 §e§lNíVEL").apply { spawn() }

    private fun updateHologram(users: MutableList<HelixUser>) {
        for (i in 0 until 10) {
            val prefix = "§e${i + 1}."
            val text = users.takeIf { it.size >= i + 1 }?.run {
                val user = get(i)
                val level = user.duels.level
                val duelsLevel = DuelsLevel(level)
                "${user.mainRankLife.rank.color}${user.name} §8- ${duelsLevel.color()}[$level${duelsLevel.symbol()}]"
            } ?: "§7[...]"
            val final = "$prefix $text"

            if (hologram.getLine(i + 1) != null) {
                hologram.updateLine(i + 1, final)
            }else {
                hologram.addLine(final)
            }
        }
        hologram.respawn()
    }

    fun runTask() = plugin.server.scheduler.runTaskTimer(plugin, {
        if (Bukkit.getOnlinePlayers().isEmpty() && hologram.lines.size > 1) return@runTaskTimer
        val users = mutableListOf<HelixUser>()

        plugin.apiBukkit.components.storage.newConnection.use {
            val query = it.query("select * from accounts order by json_extract(data, '\$.duels.level') desc limit 10")
                ?: throw NullPointerException("invalid query")

            while(query.next()) {
                val data = query.getString("data")
                val user = HelixComponents.MOSHI.adapter(HelixUser::class.java)
                    .fromJson(data) ?: throw NullPointerException("invalid user")
                users.add(user)
            }
        }
        updateHologram(users)
    }, 0, 4 * (60 * 20))
}