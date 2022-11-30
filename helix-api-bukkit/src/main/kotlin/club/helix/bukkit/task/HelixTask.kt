package club.helix.bukkit.task

import club.helix.bukkit.HelixBukkit

open abstract class HelixTask(val apiBukkit: HelixBukkit) {
    abstract fun start()
}