package util

import java.text.DecimalFormat

actual fun Double.formatDecimal(maxFractionDigits: Int): String =
    DecimalFormat().apply {
        isGroupingUsed = false
        minimumFractionDigits = 0
        maximumFractionDigits = maxFractionDigits
        isDecimalSeparatorAlwaysShown = false
    }.format(this)