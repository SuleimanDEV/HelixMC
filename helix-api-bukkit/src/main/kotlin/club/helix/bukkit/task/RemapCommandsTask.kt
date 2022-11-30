package club.helix.bukkit.task

import club.helix.bukkit.HelixBukkit

class RemapCommandsTask(apiBukkit: HelixBukkit): HelixTask(apiBukkit) {

    override fun start() {
        apiBukkit.server.scheduler.runTaskLater(apiBukkit, { execute() }, 5 * 20)
    }

    private fun execute() = apiBukkit.commandMap.run {
        remapCommands()
        unregisterCommand(
            "me", "op", "deop", "icanhasbukkit", "ver", "version", "about", "?", "pl", "plugins", "spawnpoint",
            "say", "testforblocks", "defaultgamemode", "achievement", "citizens", "citizens2", "npc", "npc2"
        )
    }
}