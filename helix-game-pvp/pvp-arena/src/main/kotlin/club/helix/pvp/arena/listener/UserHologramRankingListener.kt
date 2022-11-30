package club.helix.pvp.arena.listener

import club.helix.bukkit.event.PlayerDeathEvent
import club.helix.bukkit.kotlin.player.account
import club.helix.components.HelixComponents
import club.helix.components.account.HelixRank
import club.helix.components.account.HelixUser
import club.helix.components.kotlin.number.DecimalFormat.Companion.decimalFormat
import club.helix.hologram.Hologram
import club.helix.hologram.line.HologramPacketLine
import club.helix.pvp.arena.PvPArena
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent

class UserHologramRankingListener(
    private val plugin: PvPArena
): Listener {
    companion object {
        const val USERS_PER_PAGE = 10
        const val MAX_PAGE = 10
        private val rankingUsers = mutableListOf<HelixUser>()
    }

    init {
        plugin.server.scheduler.runTaskTimerAsynchronously(plugin, {
            rankingUsers.clear()

            plugin.apiBukkit.components.storage.newConnection.use {
                val query = it.query("select * from accounts order by json_extract(data, '\$.pvp.arena.kills') desc limit 200")
                    ?: throw NullPointerException("invalid query")

                while(query.next()) {
                    val data = query.getString("data")
                    val user = HelixComponents.MOSHI.adapter(HelixUser::class.java)
                        .fromJson(data) ?: throw NullPointerException("invalid user")

                    if (HelixRank.staff(user.mainRankLife.rank)) {
                        continue
                    }

                    rankingUsers.add(user)
                    updateUsers()
                }
            }
        }, 20L, 4 * (60 * 20))
    }

    @EventHandler fun onJoin(event: PlayerJoinEvent) = event.apply {
        val hologram = Hologram(
            Location(player.location.world, 31.60590207422695, 208.0, -7.492677280719121)
        ).apply {
            metadata.set("type", "users-ranking")
            update(player, this)
            addLine(HologramPacketLine(player, "§d§lClique para alternar!"))
            spawn()
        }

        hologram.addConsumer<PlayerInteractEvent> {
            it.player.playSound(it.player.location, Sound.CLICK, 10.0f, 10.0f)
            update(it.player, hologram)
        }
    }

    @EventHandler fun onRespawn(event: PlayerDeathEvent) = getRankingHologram(event.player)?.run {
        plugin.server.scheduler.runTaskLater(plugin, { spawn() }, 10)
    }

    private fun getRankingHologram(player: Player) = Hologram.holograms.firstOrNull {
        it.getLine<HologramPacketLine>(0)?.let {
                line -> line.player == player
        } == true
    }

    private fun updateUsers() = Hologram.holograms.filter {
        it.metadata.has("type", "users-ranking") && it.getLine<HologramPacketLine>(0) != null
    }.forEach {
        val player = it.getLine<HologramPacketLine>(0)!!.player
        update(player, it)
    }

    private fun update(player: Player, hologram: Hologram) {
        var page = hologram.metadata.get<Int>("page")?.takeIf { it <= MAX_PAGE } ?: 0

        if (page.inc() > MAX_PAGE) {
            page = 0
            hologram.metadata.set("index", 0)
        }

        "§a§lTOP ${USERS_PER_PAGE * MAX_PAGE} §e§lKILLS! §7(${page.inc()}/$MAX_PAGE)".let {
            if (hologram.hasLine(0)) {
                hologram.getLine<HologramPacketLine>(0)?.text(it)
            }else hologram.addLine(HologramPacketLine(player, it))
        }

        for (i in 0 until USERS_PER_PAGE) {
            val index = (hologram.metadata.get("index") ?: 0).apply {
                hologram.metadata.set("index", this.inc())
            }

            val prefix = "§e${index.inc()}."

            val body = rankingUsers.takeIf { it.size >= index.inc() }?.run {
                val user = get(index)
                "${user.mainRankLife.rank.color}${user.name} §8- §e${user.pvp.arena.kills.decimalFormat()}"
            } ?: "§7[...]"

            val final = "$prefix $body"

            val hologramLine = i.inc()

            if (hologram.hasLine(hologramLine)) {
                hologram.getLine<HologramPacketLine>(hologramLine)?.text(final)
            }else hologram.addLine(HologramPacketLine(player, final))
        }

        player.account.apply {
            val userLine = USERS_PER_PAGE.inc()
            val text = "§7???. ${mainRankLife.rank.color}$name §8- §e${pvp.arena.kills.decimalFormat()}"

            if (hologram.hasLine(userLine)) {
                hologram.getLine<HologramPacketLine>(USERS_PER_PAGE.inc())?.text(text)
            }else {
                hologram.addLine(HologramPacketLine(player, text))
            }
        }

        hologram.metadata.set("page", page.inc())
    }
}