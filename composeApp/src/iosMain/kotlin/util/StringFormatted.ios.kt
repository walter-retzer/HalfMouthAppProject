package util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

@OptIn(ExperimentalForeignApi::class)
actual fun Double.formatDecimal(maxFractionDigits: Int): String =
    NSNumberFormatter().apply {
        minimumFractionDigits = 0u
        maximumFractionDigits = maxFractionDigits.convert()
        numberStyle = NSNumberFormatterDecimalStyle
    }.stringFromNumber(number = NSNumber(double = this)) ?: ""
