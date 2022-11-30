package club.helix.components.account

enum class HelixRank(
    val displayName: String,
    val color: String,
    val prefix: String,
    val aliases: MutableList<String> = mutableListOf()
) {

    DIRETOR("Diretor", "§c", "§c§lDIRETOR §c", mutableListOf()),

    ADMIN("Admin", "§4", "§4§lADMIN §4", mutableListOf("administrador", "adm")),

    GERENTE("Gerente", "§3", "§3§lGERENTE §3"),


    MOD_PLUS("Moderador+", "§5", "§5§lMOD+ §5", mutableListOf("mod+")),

    MOD("Moderador", "§5", "§5§lMOD §5"),

    AJUDANTE("Ajudante", "§d", "§d§lAJUDANTE §d", mutableListOf("ajd")),


    BUILDER("Builder", "§9", "§9§lBUILDER §9", mutableListOf("construtor")),

    DESIGNER("Designer", "§b", "§b§lDESIGNER §b"),

    PROMOTOR("Promotor", "§b", "§b§lPROMOTOR §b"),

    YOUTUBER_PLUS("Youtuber+", "§3", "§3§lYT+ §3", mutableListOf("yt+")),

    YOUTUBER("Youtuber", "§b", "§b§lYT §b", mutableListOf("youtube", "yt", "partner", "parceiro")),

    BETA("Beta", "§1", "§1§lBETA §1"),

    ULTRA("Ultra", "§d", "§d§lULTRA §d"),

    PRO("Pro", "§6", "§6§lPRO §6"),

    VIP("Vip", "§a", "§a§lVIP §a"),

    BLADE("Blade", "§e", "§e§lBLADE §e"),

    NATAL("Natal", "§c", "§c§lNATAL §c"),

    DEFAULT("Membro", "§7", "§7", mutableListOf("membro", "member"));

    companion object {
        fun get(name: String): HelixRank? = values().firstOrNull {
            it.displayName.lowercase() == name.lowercase() || it.aliases.any { alias -> alias.lowercase() == name.lowercase() }
                    || it.toString().lowercase() == name.lowercase()
        }

        fun vip(rank: HelixRank) = rank.isBiggerThen(BLADE)
        fun staff(rank: HelixRank) = rank.isBiggerThen(AJUDANTE)
    }

    val permissions: MutableList<String> = mutableListOf()

    fun hasPermission(permission: String) = permissions.any {
        it == "*" || it.lowercase() == permission.lowercase()
    }

    fun addPermission(permission: String) = permissions.add(permission)

    val coloredName = (color.trim() + displayName.trim())

    fun isBiggerThen(role: HelixRank): Boolean = (ordinal <= role.ordinal)
    fun isLessThen(role: HelixRank): Boolean = (ordinal > role.ordinal)
}