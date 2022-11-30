package club.helix.components.util

import java.util.regex.Pattern

class CensureKeyWords {
    companion object {
        private val KEY_WORDS = arrayOf(
            "fdp", "desgraçado", "lixo", "buceta", "pinto", "babaca", "baitola", "besta", "bixa", "bicha",
            "boceta", "boiola", "boquete", "sexo", "tranzar", "transar", "brioco", "bronha", "bunduda", "gostosa",
            "canalha", "checheca", "chereca", "chupado", "cocaina", "cretina", "cretino", "fode", "fudendo", "idiota",
            "imbecil", "masturbar", "mijo", "pau", "pênis", "piroco", "piroca", "rabão", "rabuda", "retardado", "retardada",
            "tezão", "vagabunda", "vagabundo", "puta", "pulta", "filho da puta", "gozo", "gozar", "gozei", "otario",
            "piranha", "vadia", "gorda", "arrombado", "arrombada", "desgraça", "cu", "fudido", "caralho", "merda",
            "primata", "gordola", "gordo", "mamute", "macaco", "chifrudo", "capeta", "demonio", "estupido", "estupida",
            "bucetinha", "cadela", "fodido", "fodida", "fudida", "fudido", "fuder", "cuzão", "cuzinho", "punheta", "masturbação",
            "piroquinha", "pauzudo", "jiromba", "putinha", "nerdola", "xota", "xerecuda ", "xereca", "safadinha", "safada", "gostosinha",
            "anal", "ejaculação", "esperma", "semen", "gozando", "estupro", "estuprar", "viadao", "trouxa", "tetuda",
            "seios", "delicia", "cabaço"
        )

        fun matcher(message: String): String {
            var filteredMessage = message

            val messageSeperate = message.split(" ")

            KEY_WORDS.filter {
                messageSeperate.any { message -> message.lowercase() == it.lowercase() }
            }.forEach {
                val censuredSymbol = "*".repeat(it.length)

                filteredMessage = Pattern.compile(it, Pattern.CASE_INSENSITIVE).matcher(message)
                    .replaceAll(censuredSymbol)
            }


            return filteredMessage
        }
    }
}