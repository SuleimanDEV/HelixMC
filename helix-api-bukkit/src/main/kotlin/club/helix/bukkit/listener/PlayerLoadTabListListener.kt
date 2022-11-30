package club.helix.bukkit.listener

import club.helix.bukkit.nms.TabListNMS
import club.helix.components.util.HelixAddress
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerLoadTabListListener: Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) = TabListNMS.set(event.player,
        "\n§b§lHELIX\n",
        //"§r                                                                                                \n" +
        "§r                                                                           \n" +
                "§bSite: §f${HelixAddress.DOMAIN}\n" +
                "§bDiscord: §f${HelixAddress.DISCORD}\n\n" +
                "§e     Adquira planos §a§lVIPS §eacessando: §f§n${HelixAddress.SHOP}\n ")
}