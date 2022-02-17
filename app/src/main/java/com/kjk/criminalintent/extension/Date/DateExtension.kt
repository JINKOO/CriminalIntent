package com.kjk.criminalintent.extension.Date

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

const val FORMAT_LONG = "EEEE, dd, MMMM, yyyy"

fun Date.dateFormatLong(): String {
    val formatter = SimpleDateFormat(FORMAT_LONG, Locale.ENGLISH)
    return formatter.format(this)
}

fun Date.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault())
}

fun LocalDateTime.dateFormatLong(): String {
    return this.format(DateTimeFormatter.ofPattern(FORMAT_LONG))
}
