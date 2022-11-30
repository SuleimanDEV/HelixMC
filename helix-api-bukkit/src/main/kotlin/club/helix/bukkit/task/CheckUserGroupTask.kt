package club.helix.bukkit.task

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.nms.NameTagNMS
import club.helix.components.account.HelixRank
import club.helix.components.util.HelixAddress
import org.bukkit.Bukkit

class CheckUserGroupTask(apiBukkit: HelixBukkit): HelixTask(apiBukkit) {

    override fun start() {
        apiBukkit.server.scheduler.runTaskTimer(apiBukkit, { execute() }, 1 * (60 * 20), 1 * (60 * 20))
    }

    private fun execute() = apiBukkit.components.userManager.users.values.filter {
        it.ranksLife.any { rankLife -> !rankLife.permanent && rankLife.time < System.currentTimeMillis()}
                && Bukkit.getPlayer(it.name) != null
    }.forEach { user ->
        val expireRanksLife = user.ranksLife.filter {
            it.rank != HelixRank.DEFAULT && !it.permanent && it.time < System.currentTimeMillis()
        }
        val expireRank = expireRanksLife.first().rank

        user.removeRank(expireRank)
        user.tag = user.mainRankLife.rank
        apiBukkit.components.userManager.saveAll(user)

        val player = Bukkit.getPlayer(user.name) ?: return@forEach
        val ranksLeft = user.ranksLife.filter { it.rank != HelixRank.DEFAULT }.size

        apiBukkit.permissionManager.recalculate(player)
        NameTagNMS.setNametag(player, user.tag, NameTagNMS.Reason.TAG)

        player.sendMessage("§aSeu rank ${expireRank.color}${expireRank.displayName} §aexpirou!",)

        if (expireRanksLife.isNotEmpty()) {
            player.sendMessage("§eRestaram §c$ranksLeft ${if (ranksLeft > 1) "ranks" else "rank"}§e. §c(/ranks)")
        }

        player.sendMessage("§aAdquira ranks acessando: ${HelixAddress.SHOP}")
    }
}