package club.helix.bukkit.command

import club.helix.bukkit.HelixBukkit
import club.helix.bukkit.kotlin.player.account
import club.helix.bukkit.precommand.executor.BukkitCommandExecutor
import club.helix.bukkit.precommand.sender.BukkitCommandSender
import club.helix.components.account.HelixUserLogin
import club.helix.components.command.executor.CommandOptions
import club.helix.components.command.executor.CommandTarget
import club.helix.components.util.HelixTimeData
import org.mindrot.jbcrypt.BCrypt
import java.util.concurrent.TimeUnit

class ChangePasswordCMD(private val apiBukkit: HelixBukkit): BukkitCommandExecutor() {

    @CommandOptions(
        name = "changepassword",
        target = CommandTarget.PLAYER,
        description = "Alterar sua senha",
        aliases = ["alterarsenha"]
    )
    override fun execute(sender: BukkitCommandSender, args: Array<String>) {
        val user = sender.player.account

        if (user.login.type == HelixUserLogin.Type.PREMIUM) {
            return sender.sendMessage("§cComando disponível apenas para jogadores piratas.")
        }

        if (args.size < 2) {
            return sender.sendMessage("§aUtilize §f/alterarsenha <senha antiga> <senha nova>§a.")
        }

        if (!BCrypt.checkpw(args[0], user.login.password!!)) {
            return sender.sendMessage("§cSenha atual está incorreta.")
        }

        val newPassword = args[1].takeIf { it.length in 3..16 }
            ?: return sender.sendMessage("§cA nova senha precisa ter entre 3 e 16 caracteres.")

        if (HelixTimeData.getOrCreate(sender.name, "change-password-cmd", 3, TimeUnit.MINUTES)) {
            return sender.sendMessage("§cAguarde ${HelixTimeData.getTimeFormatted(sender.name, "change-password-cmd")} para alterar a senha novamente.")
        }

        try {
            user.login.password = BCrypt.hashpw(newPassword, BCrypt.gensalt())
            apiBukkit.components.userManager.saveAll(user)
            sender.sendMessage("§aSua senha foi atualizada!")
        }catch (error: Exception) {
            error.printStackTrace()
            sender.sendMessage("§cOcorreu um erro ao alterar sua senha.")
        }
    }
}