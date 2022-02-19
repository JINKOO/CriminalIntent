package com.kjk.criminalintent.data

import androidx.lifecycle.LiveData
import java.util.*

interface CrimeDataSender {
    fun getCrimeList(): List<Crime>
    fun getDateFormatString(date: Date): String // Date와 SimpleDateFormat사용
    fun getLocalDateFormatString(date: Date): String    // LocalDateTime과 DateTimeFormatter사용
}