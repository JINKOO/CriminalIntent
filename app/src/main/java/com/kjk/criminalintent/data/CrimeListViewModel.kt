package com.kjk.criminalintent.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

// 여기서의 viewModel은 MVVM이 아니라, 런타임 시 장치 회전으로 인한 장치 구성 변경이 발생 했을 때,
// UI상태 유지를 위한 AAC의 ViewModel이다. (생명주기)
class CrimeListViewModel : ViewModel()/*, CrimeDataSender*/ {

//    init {
//        for (i in 0 until MAX_DATA_SIZE) {
//            val crime = CrimeEntity()
//            crime.title = "Crime #$i"
//            crime.isSolved = i % 2 == 0
//            crime.requiresPolice = i % 5 == 0
//            crimes += crime
//        }
//    }

//    private val crimes = mutableListOf<CrimeEntity>()

    private val crimeRepository = CrimeRepository.get()
    val crimes = crimeRepository.getCrimes()
    val crimeLiveData = crimeRepository.getCrimes()


//    override fun getCrimeList(): List<Crime> {
//        return this.crimes
//    }
//
//    // TODO 10장 챌린지
//    override fun getDateFormatString(date: Date): String {
//        val formatter = SimpleDateFormat("E, dd, MMMM, yyyy", Locale.KOREA)
//        return formatter.format(date)
//    }
//
//    // TODO 10장 챌린지
//    override fun getLocalDateFormatString(date: Date): String {
//        val localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
//        return localDateTime.format(DateTimeFormatter.ofPattern("EEEE, dd, MMMM, yyyy", Locale.ENGLISH))
//    }

    companion object {
        private const val MAX_DATA_SIZE = 100
    }
}