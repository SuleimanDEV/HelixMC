package club.helix.bungeecord.automessage

import club.helix.components.util.HelixAddress
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder

enum class AutoMessage(val component: Array<BaseComponent>) {

    REPORT(ComponentBuilder(
        "Denuncie infratores utilizando ").color(ChatColor.YELLOW)
        .append("/report <jogador> <motivo>§e.").color(ChatColor.GOLD).create()),

    DISCORD(ComponentBuilder(
        "Faça parte de nossa comunidade: ").color(ChatColor.YELLOW)
        .append(HelixAddress.DISCORD).color(ChatColor.BLUE).create()),

/*    CHRISTMAS(ComponentBuilder(
        ""
    ));*/
}