package club.helix.components.clan

enum class ClanRole(
    val displayName: String,
    val color: String
) {
    LEADER("Líder", "§4"),
    MANAGER("Gerente", "§c"),
    MEMBER("Membro", "§7");
}