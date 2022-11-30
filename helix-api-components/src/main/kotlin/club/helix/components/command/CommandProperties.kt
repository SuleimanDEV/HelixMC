package club.helix.components.command

class CommandProperties(
    val name: String,
    val label: String,
    val permissionMessage: String = "",
    val permission: String? = null,
)