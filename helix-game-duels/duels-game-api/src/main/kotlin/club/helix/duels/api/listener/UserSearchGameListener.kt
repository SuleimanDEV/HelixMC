package club.helix.duels.api.listener

import club.helix.duels.api.DuelsAPI
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserSearchGameListener(
    private val api: DuelsAPI<*>,
): Listener {

    @EventHandler fun onJoin(event: PlayerJoinEvent) = event.player.apply {
        val request = api.requestController.load(name) ?: run {
            println("[DUELS REQUEST] Normal handle method...")
            val game = api.findAvailableGame().apply {
                println("Enviando $name para um jogo existente")
            } ?: api.newGame().apply { println("Enviando $name para um NOVO JOGO") }

            return@apply game.addPlayer(this)
        }
        api.requestController.delete(name)

        if (request.method != "SPECTATE") {
            return@apply kickPlayer("§cRequest Method inválido.")
        }

        val target = request.options["target"]?.takeIf {
            Bukkit.getPlayer(it.toString()) != null
        }?.toString() ?: return@apply kickPlayer("§cSpectate Target inválido.")


        val targetPlayer = Bukkit.getPlayer(target)
        val game = api.findGame(targetPlayer)
            ?: return@apply kickPlayer("§cPartida não encontrada.")

        game.addSpectator(this)
        teleport(targetPlayer)
    }
}