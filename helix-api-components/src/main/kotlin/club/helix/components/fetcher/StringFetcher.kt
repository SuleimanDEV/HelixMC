package club.helix.components.fetcher

import java.util.regex.Pattern

class StringFetcher {
    companion object {
        private val numbersPattern = Pattern.compile("[0-9]")
        private val lettersPattern = Pattern.compile("[A-Za-z]")

        fun getNumbers(value: String) = lettersPattern.matcher(value).replaceAll("")
        fun getLetters(value: String) = numbersPattern.matcher(value).replaceAll("")
    }
}