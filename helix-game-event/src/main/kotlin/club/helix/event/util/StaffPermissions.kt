package club.helix.event.util

import club.helix.components.account.HelixRank

class StaffPermissions {
    companion object {
        fun load() = HelixRank.PROMOTOR.run {
            addPermission("helix.event.notify")
            addPermission("helix.cmd.clearblocks")
            addPermission("helix.cmd.cleardrops")
            addPermission("helix.cmd.getconfig")
            addPermission("helix.cmd.setconfig")
            addPermission("helix.cmd.setspawn")
            addPermission("helix.cmd.settime")
            addPermission("helix.cmd.setwinner")
            addPermission("helix.cmd.skit")
            addPermission("helix.cmd.skit")
            addPermission("helix.cmd.tpall")
            addPermission("helix.cmd.whitelist")
            addPermission("helix.cmd.build")
            addPermission("helix.cmd.time")
            addPermission("helix.cmd.broadcast")
            addPermission("minecraft.command.difficulty")
            addPermission("minecraft.command.give")
            addPermission("minecraft.command.enchant")
            addPermission("minecraft.command.effect")
            addPermission("minecraft.command.kill")
            addPermission("minecraft.command.worldborder")
            addPermission("worldguard.*")
            addPermission("worldedit.*")
        }
    }
}