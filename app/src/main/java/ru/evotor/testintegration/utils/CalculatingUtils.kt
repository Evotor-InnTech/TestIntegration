package ru.evotor.testintegration.utils

import java.math.BigDecimal
import java.math.RoundingMode

const val TWO_DIGITS_PRECISION = 2

fun BigDecimal.roundForTwoDigits(): BigDecimal =
    this.setScale(TWO_DIGITS_PRECISION, RoundingMode.HALF_UP)