package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import java.io.StringReader
import java.util.*

class JedisCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    @CommandOptions(
        name = "jedis",
        permission = true,
        description = "Verificar informações do Jedis.",
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val info = apiBukkit.components.redisPool.resource.use { it.info() }
        val props = Properties().apply { load(StringReader(info)) }
        val uptime = props["uptime_in_seconds"].toString().toInt()

        sender.sendMessage(arrayOf(
            "",
            "§cInformações do Jedis (v${props["redis_version"]}):",
            "§fPool Status: ${if (!apiBukkit.components.redisPool.isClosed) "§aConectado §7(${props["role"].toString().uppercase()})" else "§aDesconectado"}",
            "§fCPU: §2${props["used_cpu_sys"]}",
            "§fMemória: §b${props["used_memory"]}",
            "§fUptime: §7${uptime / 60}m ${uptime % 60}s",
            "",
            "§fClientes conectados: §a${props["connected_clients"]}",
            "§fTotal de conexões recebidas: §e${props["total_connections_received"]}",
            "§fTotal de comandos processados: §e${props["total_commands_processed"]}",
            "§fCanais pub/sub: §6${props["pubsub_channels"]}",
            ""
        ))
    }
}