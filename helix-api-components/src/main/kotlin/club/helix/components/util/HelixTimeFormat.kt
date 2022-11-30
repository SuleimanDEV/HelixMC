package club.helix.components.util

import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class HelixTimeFormat {
    companion object {

        fun format(inputTime: Long, calculeDifference: Boolean = false): String {
            var time = inputTime

            if (calculeDifference)  {
                time -= System.currentTimeMillis()
            }

            val days = time / (24 * 60 * 60 * 1000)
            val hours = time / (60 * 60 * 1000) % 24
            val minutes = time / (60 * 1000) % 60
            val seconds = time / 1000 % 60

            return StringBuilder().apply {
                if (days > 0) append("${days}d").append(" ")
                if (hours > 0) append("${hours}h").append(" ")
                if (minutes > 0) append("${minutes}m").append(" ")

                if (toString().trim().isNotEmpty()) {
                    if (seconds > 0) append("${seconds}s")
                }else {
                    val decimalFormat = DecimalFormat("0.0")
                    append(decimalFormat.format(time.toDouble() / 1000)).append("s")
                }

                if (toString().isEmpty()) {
                    append("{expirado}")
                }
            }.toString()
        }

        fun getTimeUnit(str: String) = when (str.lowercase()) {
            "s", "second", "seconds", "segundo", "segundos" -> TimeUnit.SECONDS
            "m", "minute", "minutes", "minuto", "minutos" -> TimeUnit.MINUTES
            "h", "hour", "hours", "hora", "horas" -> TimeUnit.HOURS
            "d", "day", "days", "dia", "dias" -> TimeUnit.DAYS
            else -> null
        }
    }
}