package club.helix.components.account

enum class HelixMedal(
    val displayName: String,
    val icon: String,
    val color: String,
) {
    SPIRAL("Aspiral", "❖", "§3"),
    VERIFIED("Verificado", "✔", "§a"),
    GIFT_HUNTER("Caçador de Presentes", "✪", "§e"),
    AMOR("Amor", "❤", "§4"),
    STAR("Estrela", "✮", "§e"),
    FLOR("Flor", "❀", "§d"),
    YINGYANG("Ying Yang", "☯", "§f"),
    RADIATION("Radiação", "☣", "§a"),
    MUSIC("Músico", "♪", "§5"),
    PAZAMOR("Paz e Amor", "✌", "§c"),
    COFFE("Café", "☕", "§e"),
    FLASH("Flash", "⚡", "§6"),
    UMBRELLA("Guarda-Chuva", "☔", "§b"),
    DEFAULT("Nenhuma", "", "§7");

    companion object {
        fun get(name: String) = values().firstOrNull {
            it.toString().lowercase() == name.lowercase() ||
                    it.toString().replace("-", "").lowercase() == name.lowercase() ||
                    it.displayName.lowercase() == name.lowercase() ||
                    it.displayName.replace("-", "").lowercase() == name.lowercase()
        }
    }
}