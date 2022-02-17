package com.kjk.criminalintent.data

import java.time.LocalDateTime
import java.util.*

interface CrimeDataSender {
    fun getCrimeList(): MutableList<CrimeEntity>
    fun getDateFormatString(date: Date): String // Date와 SimpleDateFormat사용
    fun getLocalDateFormatString(date: Date): String    // LocalDateTime과 DateTimeFormatter사용
}