package com.kjk.criminalintent.data

import androidx.lifecycle.ViewModel

// 여기서의 viewModel은 MVVM이 아니라, 런타임 시 장치 회전으로 인한 장치 구성 변경이 발생 했을 때,
// UI상태 유지를 위한 AAC의 ViewModel이다. (생명주기)
class CrimeListViewModel : ViewModel(), CrimeDataSender {

    private val crimes = mutableListOf<CrimeEntity>()

    init {
        for (i in 0 until MAX_DATA_SIZE) {
            val crime = CrimeEntity()
            crime.title = "Crime #$i"
            crime.isSolved = i % 2 == 0
            crime.requiresPolice = i % 5 == 0
            crimes += crime
        }
    }

    override fun getCimeList(): MutableList<CrimeEntity> {
        return this.crimes
    }

    companion object {
        private const val MAX_DATA_SIZE = 100
    }
}