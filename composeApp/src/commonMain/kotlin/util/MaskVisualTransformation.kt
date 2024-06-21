package util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.absoluteValue

class MaskVisualTransformation(private val mask: String) : VisualTransformation {

    private val specialSymbolsIndices = mask.indices.filter { mask[it] != '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        text.forEach { char ->
            while (specialSymbolsIndices.contains(maskIndex)) {
                out += mask[maskIndex]
                maskIndex++
            }
            out += char
            maskIndex++
        }
        return TransformedText(AnnotatedString(out), offsetTranslator())
    }

    private fun offsetTranslator() = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val offsetValue = offset.absoluteValue
            if (offsetValue == 0) return 0
            var numberOfHashtags = 0
            val masked = mask.takeWhile {
                if (it == '#') numberOfHashtags++
                numberOfHashtags < offsetValue
            }
            return masked.length + 1
        }

        override fun transformedToOriginal(offset: Int): Int {
            return mask.take(offset.absoluteValue).count { it == '#' }
        }
    }

    companion object {
        const val PHONE = "(##) #####-####"
    }
}

fun String.formattedAsPhone(): String {
    return when (length) {
        11 -> "(" + substring(0, 2) + ") " +
                substring(2, 3) + " " +
                substring(3, 7) + "-" +
                substring(7, length)
        10 -> "(" + substring(0, 2) + ") " +
                substring(2, 6) + "-" +
                substring(6, length)

        14 -> substring(0, 3) + " (" + substring(3, 5) + ") " +
                substring(5, 7) +
                substring(7, 10) + "-" +
                substring(10, length)

        else -> this
    }
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun String.formattedAsDate(): String {
    val formatPattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    return try {
        val dateTimeFormat = LocalDateTime.Format { byUnicodePattern(formatPattern) }
        val dateTimeReceiver = dateTimeFormat.parse(this)
        dateTimeReceiver.dayOfMonth.toString() + "/" + dateTimeReceiver.monthNumber + "/" + dateTimeReceiver.year
    } catch (e: Exception) {
        println("Erro ao Converter a String: $e")
        "##/##/####"
    }
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun String.formattedAsTime(): String {
    val formatPattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    val timeZone = TimeZone.of("America/Los_Angeles")

    return try {
        val dateTimeFormat = LocalDateTime.Format { byUnicodePattern(formatPattern) }
        val dateTimeReceiver = dateTimeFormat.parse(this)
        val adjustTimeZone = dateTimeReceiver.toInstant(timeZone)
        val time = adjustTimeZone.minus(3, DateTimeUnit.HOUR, timeZone).toLocalDateTime(timeZone)
        var minute = time.minute.toString()
        var second = time.second.toString()
        if(minute.length == 1) minute = "0$minute"
        if(second.length == 1) second = "0$second"

        time.hour.toString() + ":" + minute + ":" + second
        //time.hour.toString() + ":" + time.minute.toString() + ":" + time.second.toString()
    } catch (e: Exception) {
        println("Erro ao Converter a String: $e")
        "##:##:##"
    }
}

fun String.adjustString(): String {
    var text = ""
    if (this == "0.00000") text = " = Desligado"
    else if (this == "1.00000") text = " = Ligado"
    else {
        try {
            val value = this.toDouble().formatDecimal()
            text = " = ${value.replace(",", ".")}°C"
        } catch (e: Exception) {
            println("Erro ao Converter a String: $e")
        }
    }
    return text
}
