package club.helix.lobby.login.command

import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.lobby.login.LoginLobby
import club.helix.lobby.login.event.UserAuthenticateEvent
import club.helix.lobby.login.inventory.CaptchaInventory
import org.mindrot.jbcrypt.BCrypt

class LoginCMD(private val plugin: LoginLobby): BukkitCommandExecutor() {

    @CommandOptions(
        name = "login",
        target = CommandTarget.PLAYER,
        description = "Efetuar autenticação.",
        aliases = ["logar"]
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val user = sender.player.account

        if (CaptchaInventory.captchaPlayers.contains(sender.name)) {
            return sender.sendMessage("§cComplete o captcha para fazer login.")
        }

        if (!user.login.isRegistered()) {
            return sender.sendMessage("§cVocê não está registrado.")
        }

        if (plugin.isAuthenticate(sender.name)) {
            return sender.sendMessage("§cVocê já está autenticado.")
        }

        if (args.isEmpty()) {
            return sender.sendMessage("§eUtilize /login <senha> para autenticar-se.")
        }

        if (!BCrypt.checkpw(args[0], user.login.password)) {
            return sender.player.kickPlayer("§cSenha incorreta.")
        }

        plugin.server.pluginManager.callEvent(UserAuthenticateEvent(sender.player))
        sender.sendMessage("§aLogin efetuado com sucesso!")
    }
}