package club.helix.components.kotlin.number

import java.text.DecimalFormat

class DecimalFormat {
    companion object {
        private val decimalFormat = DecimalFormat("#,###")

        private val decimals = arrayOf("", "k", "m", "b", "t")
        private val shortnessFormat = DecimalFormat("##.#")

        fun Int.decimalFormat() = decimalFormat.format(this)
        fun Double.decimalFormat() = decimalFormat.format(this)
        fun Number.decimalFormat() = decimalFormat.format(this)
    }
}