package club.helix.components.command.executor

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandOptions(
    val name: String,
    val description: String,
    val target: CommandTarget = CommandTarget.ALL,
    val permission: Boolean = false,
    val permissionMessage: String = "§cVocê não tem permissão para executar este comando.",
    val aliases: Array<String> = []
)