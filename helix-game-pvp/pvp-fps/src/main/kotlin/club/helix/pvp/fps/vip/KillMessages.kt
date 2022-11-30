package club.helix.pvp.fps.vip

enum class KillMessages(val message: String) {
    BASIC("{killer} §7matou {victim}§7."),
    FINALIZE("{killer} §7finalizou {victim}§7."),
    FALL("{killer} §7pisou em {victim}§7."),
    FIRE("{killer} §7colocou §cFogo §7em {victim}§7."),
    SPACE("{killer} §7enviou {victim} §7para o Espaço.");
}