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

class RegisterCMD(private val plugin: LoginLobby): BukkitCommandExecutor() {

    @CommandOptions(
        name = "register",
        target = CommandTarget.PLAYER,
        description = "Registrar conta.",
        aliases = ["registrar"]
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val user = sender.player.account

        if (CaptchaInventory.captchaPlayers.contains(sender.name)) {
            return sender.sendMessage("§cComplete o captcha para fazer login.")
        }

        if (user.login.isRegistered()) {
            return sender.sendMessage("§cVocê já está registrado.")
        }

        if (args.size < 2) {
            return sender.sendMessage("§cUtilize /register <senha> <senha> para criar uma conta.")
        }

        val password = args[0]
        val confirmPassword = args[1]

        if (password != confirmPassword) {
            return sender.sendMessage("§cAs senhas não se coincidem.")
        }

        if (password.length < 3 || password.length > 16) {
            return sender.sendMessage("§cSenha muito ${if (password.length < 3) "pequena" else "grande"}.")
        }

        try {
            plugin.server.scheduler.runTaskAsynchronously(plugin) {
                user.login.password = BCrypt.hashpw(password, BCrypt.gensalt())
                plugin.apiBukkit.components.userManager.saveAll(user)

                plugin.server.pluginManager.callEvent(UserAuthenticateEvent(sender.player))
                sender.sendMessage("§aConta criada com sucesso!")
            }
        }catch (error: Exception) {
            error.printStackTrace()
            sender.sendMessage(arrayOf(
                "§cOcorreu um erro ao criar sua conta.",
                "§cEntre em contato com nossa equipe para resolver."
            ))
        }
    }
}