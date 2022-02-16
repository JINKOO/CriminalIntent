package com.kjk.criminalintent.data

interface CrimeDataSender {
    fun getCimeList(): MutableList<CrimeEntity>
}