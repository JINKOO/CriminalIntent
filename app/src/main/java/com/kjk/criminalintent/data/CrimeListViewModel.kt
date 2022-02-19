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
class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    val crimeLiveData = crimeRepository.getCrimes()

    companion object {
        private const val TAG = "CrimeListViewModel"
        private const val MAX_DATA_SIZE = 100
    }
}