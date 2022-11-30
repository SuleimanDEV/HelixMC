package club.helix.components.account

enum class SkinsLibrary(
    val displayName: String,
    val skinId: String,
    val createdAt: Long
) {
    SMART("Smart", "SmarttBR", 1637623323697),
    JOCTOR("Joctor", "Joctor", 1637623323697),
    COUTZ("Coutz", "Coutz_", 1637775147263),
    HARIES("Haries", "Haries", 1637775147263),
    ZAK("Zak", "ZAKl1k", 1637775198560),
    STEVE("Steve", "oduncu", 1637775110112),
    THANOS("Thanos", "Thanos", 1637630798937),
    SPIDERMAN("Homem Aranha", "jChumz", 1637630869577),
    HULK("Hulk", "BouncinBarry", 1637630905698),
    DEADPOOL("Deadpool", "akaria234", 1637630992985),
    THOR("Thor", "Pointaq", 1637631049537),
    DOUTOR_ESTRANHO("Doutor Estranho", "Vortexs_", 1637631108649),
    VENOM("Venom", "xispazzz", 1637631178673),
    IRON_MAN("Homem de Ferro", "PauloFloyd", 1637631214049),
    JOKER("Coringa", "StarSensei", 1637631287801),
    BATMAN("Batman", "EklundBoys123", 1637631332329),
    ROUND6_QUADRADO("Round 6 (Quadrado)", "sopaw", 1637631384161),
    ROUND6_TRIANGULO("Round 6 (Triângulo)", "Miloyo", 1637631423713),
    ROUND6_CIRCULO("Round 6 (Circulo)", "Pufosul1", 1637631452849),
    ROUND6_BONECA("Round 6 (Boneca)", "luanx", 1637631511376),
    PAPAI_NOEL("Papai Noel", "BoomerB", 1637631690705),
    VELHO("Velho", "Vrec", 1637631889369),
    LA_CASA_DE_PAPEL("La Casa de Papel", "JaoDoPao", 1637632037153),
    DOG_ROMANTICO("Dog Romântico", "Gamerknight964", 1637632121978),
    MAGO("Mago", "S4CKM7D1CKNOW", 1637632230561),
    WARRIOR("Guerreiro", "iTunesPlaylist", 1637632279882),

    ;

    companion object {
        fun get(name: String) = values().firstOrNull {
            it.toString().lowercase() == name.lowercase()
                    && it.displayName.lowercase() == name.lowercase()
                    && it.skinId.lowercase() == name.lowercase()
        }
    }
}