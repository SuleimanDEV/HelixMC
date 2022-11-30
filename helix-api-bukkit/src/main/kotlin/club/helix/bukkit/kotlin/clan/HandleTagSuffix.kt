package club.helix.bukkit.kotlin.clan

import club.helix.components.clan.Clan
import club.helix.bukkit.nms.NameTagNMS
import org.bukkit.ChatColor
import org.bukkit.entity.Player

fun Clan.handleTagSuffix(player: Player) {
    val tagColor = ChatColor.getByChar(tagColorCode.toString())
    println(tagColor)
    NameTagNMS.setSuffix(player, " $tagColor[$tag]")
}